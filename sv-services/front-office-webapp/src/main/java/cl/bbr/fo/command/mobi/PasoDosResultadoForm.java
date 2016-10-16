/*
 * Created on 19-ago-2008
 */
package cl.bbr.fo.command.mobi;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocCollector;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.command.Command;
import cl.bbr.fo.util.busqueda.Buscar;
import cl.bbr.fo.util.busqueda.Ortografia;
import cl.bbr.fo.util.busqueda.Sinonimos;
import cl.bbr.fo.util.busqueda.SpanishAnalyzer;
import cl.bbr.jumbocl.common.utils.DString;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.productos.dto.CategoriaDTO;
import cl.bbr.jumbocl.productos.dto.DestacadoDTO;
import cl.bbr.jumbocl.productos.dto.MarcaDTO;
import cl.bbr.jumbocl.productos.dto.ProductoCarroDTO;
import cl.bbr.jumbocl.productos.dto.ProductoDTO;
import cl.bbr.promo.lib.dto.PromocionDTO;

/**
 * @author jdroguett
 */
public class PasoDosResultadoForm extends Command {
   private BizDelegate biz;
   private File indexDir;
   private File indexDirOrt;
   private Hashtable cantidadesCalculadas = new Hashtable();

   public PasoDosResultadoForm() {
      ResourceBundle rb = ResourceBundle.getBundle("fo");
      indexDir = new File(rb.getString("path.buscador"));
      indexDirOrt = new File(rb.getString("path.ortografia"));
      biz = new BizDelegate();
   }

   protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
      try {
         long ini0 = System.currentTimeMillis();
         arg0.setCharacterEncoding("UTF-8");
         HttpSession session = arg0.getSession();
         arg1.setContentType("text/html; charset=iso-8859-1");
         // Carga properties
         ResourceBundle rb = ResourceBundle.getBundle("fo");

         IValueSet top = new ValueSet();
         PrintWriter out = arg1.getWriter();

         String pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form");

         TemplateLoader load = new TemplateLoader(pag_form);
         ITemplate tem = load.getTemplate();

         //////////////////////////////////////////////////////////
         String verFotos = arg0.getParameter("ver_fotos");
         //////////////////////////////////////////////////////////
         //para el paginado
         String start = arg0.getParameter("start");
         int filaNumero = 1;
         if (start != null)
            filaNumero = Integer.parseInt(start);
         //////////////////////////////////////////////////////////
         int idLocal = Integer.parseInt((String) session.getAttribute("ses_loc_id"));
         int idCliente = Integer.parseInt((String) session.getAttribute("ses_cli_id"));
         String accion = arg0.getParameter("accion");
         this.getLogger().debug("Accion: " + accion);
         //////////////////////////////////////////////////////////
         String ordenarPor = arg0.getParameter("ordenar_por");
         this.getLogger().debug("ordenar_por: " + ordenarPor);

         if (ordenarPor == null)
            ordenarPor = "default";
         //////////////////////////////////////////////////////////
         int filaCantidad = 10;
         String sMostrarCantidad = arg0.getParameter("mostrar_cantidad");
         this.getLogger().debug("sMostrarCantidad: " + sMostrarCantidad);
         if (sMostrarCantidad == null)
            sMostrarCantidad = (String) session.getAttribute("mostrar_cantidad");
         if (sMostrarCantidad != null) {
            filaCantidad = Integer.parseInt(sMostrarCantidad);
            if (filaCantidad < 10)
               filaCantidad = 10;
         }
         session.setAttribute("mostrar_cantidad", filaCantidad + "");
         //////////////////////////////////////////////////////////

         int idCategoria = 0;
         int idSubCategoria = 0;
         int idMarca = 0;

         String sIdCategoria = arg0.getParameter("caip");
         String sIdSubCategoria = arg0.getParameter("cai");

         if (sIdSubCategoria == null) {
            sIdCategoria = arg0.getParameter("idCategoria");
            sIdSubCategoria = arg0.getParameter("idSubCategoria");
         }
         String sIdMarca = arg0.getParameter("idMarca");

         this.getLogger().debug("sIdSubCategoria: " + sIdSubCategoria);
         this.getLogger().debug("sIdmarca: " + sIdMarca);

         if (sIdCategoria != null)
            idCategoria = Integer.parseInt(sIdCategoria);
         if (sIdSubCategoria != null)
            idSubCategoria = Integer.parseInt(sIdSubCategoria);
         if (sIdMarca != null)
            idMarca = Integer.parseInt(sIdMarca);

         //Recuperar los cupones y los tcp de la sesión (copy paste de
         // versión anterior
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
         int total = 0;
         String palabra = "";
         String[] palabras = null;
         if ("buscar".equals(accion)) {
            long ini1 = System.currentTimeMillis();
            //////////// INICIO BUSCAR //////////////////////
            palabra = arg0.getParameter("buscar").trim();
            String sugerencias = arg0.getParameter("sugerencias");
            this.getLogger().debug("palabra a buscar: " + palabra);
            List idsProductos = Buscar.search(palabra);
            this.getLogger().debug("idsProductos.size: " + idsProductos.size());
            this.getLogger().debug("tiempo total lucene: " + (System.currentTimeMillis() - ini1));
            if (sugerencias != null && !sugerencias.equals(""))
               palabras = DString.decode(sugerencias);

            if (idsProductos.size() > 0) {
               try {
                  long ini = System.currentTimeMillis();
                  listaProductos = biz.productosPorIds(idCliente, idsProductos, idLocal, idMarca, 0, 0, ordenarPor,
                        filaNumero, filaCantidad);
                  this.getLogger().debug("tiempo buscar por ids: " + (System.currentTimeMillis() - ini));
                  listaProductos = ordenarProductos(ordenarPor, listaProductos, idsProductos);
                  //ahora obtengo las marcas de la lista de producto ver
                  // marcarDeProductos
                  //listaMarcas = biz.marcasPorIds(idsMarcas, idLocal);
                  //debido al paginador no puedo obtener el total de la forma
                  // listaProductos.size ni idsProductos.size
                  ini = System.currentTimeMillis();
                  total = biz.cantidadProductosPorIds(idsProductos, idLocal, idMarca, 0, 0);
                  this.getLogger().debug("tiempo cantidad por ids prod: " + (System.currentTimeMillis() - ini));
               } catch (Exception e) {
                  e.printStackTrace();
                  total = 0;
                  getLogger().error("Error en idsProductos.size() > 0: " + e.getMessage());
               }
//-20121105avc
//            } else { //verficar si hay falta de ortografia
//               palabras = ortografia(palabra);
//-20121105avc
            }
            this.getLogger().debug("tiempo total buscar: " + (System.currentTimeMillis() - ini1));
            //////////// FIN BUSCAR /////////////////////////
         } else { //mostrar
            long ini = System.currentTimeMillis();
            //////////
            try {
               listaProductos = biz.productosPorSubCategoria(idCliente, idLocal, idSubCategoria, idMarca, ordenarPor,
                     filaNumero, filaCantidad);
               //ahora obtengo las marcas de la lista de producto ver
               // marcarDeProductos
               //listaMarcas = biz.marcasPorSubCategoria(idLocal,
               // idSubCategoria);
               //debido al paginado no puedo obtener el total de la forma
               //listaProductos.size()
               total = biz.cantidadProductosDeSubCategoria(idLocal, idSubCategoria, idMarca);
            } catch (Exception e) {
               e.printStackTrace();
               total = 0;
            }
            this.getLogger().debug("tiempo por categoria: " + (System.currentTimeMillis() - ini));
         }

         Hashtable productosDestacados = null;
         if (listaProductos != null && listaProductos.size() > 0) {
            long ini = System.currentTimeMillis();
            listaProductos = biz.cargarPromociones(listaProductos, lista_tcp, idLocal);
            this.getLogger().debug("tiempo carga promociones: " + (System.currentTimeMillis() - ini));
            ini = System.currentTimeMillis();
            productosDestacados = biz.getProductosDestacadosDeHoy(idLocal);
            this.getLogger().debug("tiempo getProductosDestacadosDeHoy: " + (System.currentTimeMillis() - ini));
            ini = System.currentTimeMillis();
            listaMarcas = marcarDeProductos(listaProductos);
            this.getLogger().debug("tiempo lista marcas de prod: " + (System.currentTimeMillis() - ini));
         }
         int clienteId = Integer.parseInt(session.getAttribute("ses_cli_id").toString());
         String invitado_id = "";
         if (session.getAttribute("ses_invitado_id") != null &&
                 !session.getAttribute("ses_invitado_id").toString().equals("")){
             invitado_id = session.getAttribute("ses_invitado_id").toString();
         }
         long ini = System.currentTimeMillis();
         Hashtable productosIdCarro = biz.productosCarro(clienteId, invitado_id);
         this.getLogger().debug("tiempo productos del carro: " + (System.currentTimeMillis() - ini));
         List productos = productos(listaProductos, productosIdCarro, productosDestacados, "true".equals(verFotos));
         List sugerencias = sugerencias(palabras);

         List marcas = marcas(listaMarcas, idMarca);
         IValueSet resultado = new ValueSet();
         resultado.setVariable("{texto}", palabra);
         resultado.setDynamicValueSets("PRODUCTOS", productos);
         resultado.setDynamicValueSets("SUGERENCIAS", sugerencias);
         resultado.setDynamicValueSets("MARCAS", marcas);
         resultado.setVariable("{total_encontrados}", "" + total);
         int pag_total = (total % filaCantidad > 0 ? total / filaCantidad + 1 : total / filaCantidad);
         int pag_actual = (filaNumero % filaCantidad > 0 ? filaNumero / filaCantidad + 1 : filaNumero / filaCantidad);
         resultado.setVariable("{pag_total}", "" + pag_total);
         resultado.setVariable("{pag_actual}", "" + pag_actual);

         String nombreCategoria = "Todas";
         String nombreSubCategoria = "Todas";
         String banner = "";
         if (idSubCategoria != 0) {
            CategoriaDTO subcat = biz.getCategoria(idSubCategoria);
            nombreSubCategoria = subcat.getNombre();
            banner = subcat.getBanner();
            idCategoria = (int) subcat.getId_padre();
         }
         if (idCategoria != 0) {
            CategoriaDTO cat = biz.getCategoria(idCategoria);
            nombreCategoria = cat.getNombre();
         }

         resultado.setVariable("{categoria_nombre}", nombreCategoria);
         resultado.setVariable("{subcategoria_nombre}", nombreSubCategoria);
         resultado.setVariable("{banner}", (banner == null ? "" : banner));
         resultado.setVariable("{ultima_accion}", accion);
         //////////////////////////////////////////////////////////
         List anterior = new ArrayList();
         IValueSet valueset = new ValueSet();
         valueset.setVariable("{start}", (filaNumero - filaCantidad) + "");
         anterior.add(valueset);
         if (filaNumero > filaCantidad) {
            resultado.setDynamicValueSets("ANTERIOR", anterior);
         } else {
            resultado.setDynamicValueSets("ANTERIORB", anterior);
         }
         List siguiente = new ArrayList();
         valueset = new ValueSet();
         valueset.setVariable("{start}", (filaNumero + filaCantidad) + "");
         siguiente.add(valueset);
         if (filaNumero + filaCantidad <= total) {
            resultado.setDynamicValueSets("SIGUIENTE", siguiente);
         } else {
            resultado.setDynamicValueSets("SIGUIENTEB", siguiente);
         }
         //////////////////////////////////////////////////////////

         resultado.setDynamicValueSets("ORDENAR_POR", selectOrdenarPor(ordenarPor));
         resultado.setDynamicValueSets("MOSTRAR_CANTIDAD", selectMostrarCantidad(total, filaCantidad));

         //////////////////////////////////////////////////////////
         List elementos = new ArrayList();
         elementos.add(resultado);
         top.setDynamicValueSets("RESULTADO", elementos);

         //////////////////////////////////////////////////////////
         String result = tem.toString(top);
         out.print(result);
         this.getLogger().debug("Tiempo total : " + (System.currentTimeMillis() - ini0));
      } catch (Exception e) {
         e.printStackTrace();
         
         if(e.getMessage() == null) 
         	this.getLogger().error("Error en execute: " + e.getClass().getName() + " en PasoDosResultadoForm");
         else
         	this.getLogger().error("Error en execute: " + e.getMessage() + " en PasoDosResultadoForm");
      }
   }

   /**
    * @param listaProductos
    * @return
    */
   private MarcaDTO[] marcarDeProductos(List listaProductos) {
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
    * @param ordenarPor
    * @param listaProductos
    * @param idsProductos
    * @return
    */
   private List ordenarProductos(String ordenarPor, List listaProductos, List idsProductos) {
      long ini = System.currentTimeMillis();
      if ("default".equals(ordenarPor) || ordenarPor == null || ordenarPor.equals("")) {
         //organizo la listaProductos según orden de
         //idsProductos, aprovecho de eliminar duplicados
         Hashtable hash = new Hashtable();
         HashSet set = new HashSet();
         for (int i = 0; i < listaProductos.size(); i++) {
            ProductoDTO p = (ProductoDTO) listaProductos.get(i);
            hash.put("" + p.getPro_id(), p);
         }
         listaProductos = new ArrayList();
         for (int i = 0; i < idsProductos.size(); i++) {
            String id = (String) idsProductos.get(i);
            ProductoDTO p = (ProductoDTO) hash.get(id);
            if (p != null && !set.contains(id)) {
               listaProductos.add(p);
               set.add(id);
            }
         }
      }
      this.getLogger().debug("tiempo ordenar default: " + (System.currentTimeMillis() - ini));
      return listaProductos;
   }

   /**
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

   /**
    * Crea la vista de las marca a mostrar en la página
    * 
    * @param idMarca
    * @param listaProductos
    * @return
    */
   private List marcas(MarcaDTO[] listaMarcas, int idMarca) {
      List lista = new ArrayList();
      if (listaMarcas != null) {
         for (int i = 0; i < listaMarcas.length; i++) {
            IValueSet marca = new ValueSet();
            MarcaDTO marcaDTO = (MarcaDTO) listaMarcas[i];
            marca.setVariable("{id}", "" + marcaDTO.getMar_id());
            marca.setVariable("{value}", marcaDTO.getMar_nombre());
            marca.setVariable("{selected}", (marcaDTO.getMar_id() == idMarca ? "selected" : ""));
            lista.add(marca);
         }
         IValueSet marca = new ValueSet();
         marca.setVariable("{id}", "0");
         marca.setVariable("{value}", "Todas");
         marca.setVariable("{selected}", (0 == idMarca ? "selected" : ""));
         lista.add(0, marca);
      }
      return lista;
   }

   /**
    * Crea vista de los productos a mostrar en la página
    * 
    * @param productosIdCarro
    * @param list
    * @return
    */
   private List productos(List lista, Hashtable productosIdCarro, Hashtable prodDestacados, boolean verFotos) {
      List listaProductos = new ArrayList();
      if (lista != null) {
         for (int i = 0; i < lista.size(); i++) {
            ProductoDTO productoDTO = (ProductoDTO) lista.get(i);
            ProductoCarroDTO productoCarroDTO = (ProductoCarroDTO) productosIdCarro.get(new Integer((int) productoDTO.getPro_id()));
            IValueSet producto = new ValueSet();
            producto.setVariable("{id}", "" + productoDTO.getPro_id());
            producto.setVariable("{tipo}", productoDTO.getTipo_producto());
            producto.setVariable("{marca}", productoDTO.getMarca());
            producto.setVariable("{descripcion}", productoDTO.getDescripcion());
            String particion = "";
            double precio = productoDTO.getPrecio();
            if ("S".equals(productoDTO.getEsParticionable()) && productoDTO.getParticion() != 0) {
               particion = "1/" + productoDTO.getParticion() + " ";
               precio /= productoDTO.getParticion();
            }
            producto.setVariable("{precio}", Formatos.formatoPrecioFO(precio) + " " + particion
                  + Formatos.formatoUnidadFO(productoDTO.getTipre()));
            producto.setVariable("{precio_por_unidad}", Formatos.formatoPrecioFO(productoDTO.getPpum()) + " x "
                  + productoDTO.getUnidad_nombre());
            ///unidad, cantidad, mínima y máxima no se utilizan en la
            // sección
            // mobi, sólo se usan en la sección normal
            producto.setVariable("{cantidad_minima}", productoDTO.getCantidadMinima() + "");
            producto.setVariable("{cantidad_maxima}", productoDTO.getCantidadMaxima() + "");
            producto.setVariable("{unidad}", Formatos.formatoUnidadFO(productoDTO.getTipre()));
            if (productoCarroDTO != null)
               producto.setVariable("{cantidad}", Formatos.cantidadFO(productoCarroDTO.getCantidad()));
            else
               producto.setVariable("{cantidad}", Formatos.cantidadFO(productoDTO.getCantidadMinima()));
            //////////////////////////////////////
            if (verFotos) {
               List elementos = new ArrayList();
               IValueSet conFoto = new ValueSet();
               conFoto.setVariable("{imagen_chica}", "" + productoDTO.getImg_chica());
               elementos.add(conFoto);
               producto.setDynamicValueSets("CON_FOTO", elementos);
            }
            //////////////////////////////////////
            promociones(productoDTO, producto);
            destacador(productoDTO, prodDestacados, producto);
            //////////////////////////////////////
            String html = listadoDeCantidades(productoDTO.getCantidadMinima(), productoDTO.getCantidadMaxima());
            //////////////////////////////////////
            List elementos = new ArrayList();
            IValueSet value = new ValueSet();
            value.setVariable("{productoId}", "" + productoDTO.getPro_id());
            value.setVariable("{nota}", (productoDTO.getNota() == null || productoDTO.getNota().trim().equals("") ? ""
                  : productoDTO.getNota()));
            elementos.add(value);
            if (productoCarroDTO != null) {
               producto.setDynamicValueSets("IMG_MODIFICAR", elementos);
               //////////////////////////////////////
               String sCantidad = Formatos.cantidadFO(productoCarroDTO.getCantidad());
               html = html.replaceFirst("<option value=\"" + sCantidad + "\">", "<option value=\"" + sCantidad
                     + "\" selected >");
               //////////////////////////////////////
            } else {
               producto.setDynamicValueSets("IMG_AGREGAR", elementos);
            }
            if (productoDTO.isCon_nota()) {
               producto.setDynamicValueSets("IMG_CON_NOTA", elementos);
            }
            producto.setVariable("{cantidades}", html);
            listaProductos.add(producto);
         }
      }
      return listaProductos;
   }

   private void promociones(ProductoDTO productoDTO, IValueSet producto) {
      //Revisar si existen promociones para el producto
      List lst_promo = productoDTO.getListaPromociones();
      if (lst_promo != null && lst_promo.size() > 0) {
         this.getLogger().debug("Existen Promociones:" + lst_promo.size());
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

   /**
    * @param productoDTO
    * @param prodDestacados
    * @param producto
    */
   private void destacador(ProductoDTO productoDTO, Hashtable prodDestacados, IValueSet producto) {
      if (prodDestacados == null)
         return;
      if (productoDTO.getListaPromociones() != null && productoDTO.getListaPromociones().size() > 0)
         return;
      DestacadoDTO destacadoDTO = (DestacadoDTO) prodDestacados.get(new Integer(productoDTO.getPro_id_bo()));
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
    * Esto es porque fastm se pone lento al crear tantos options de un select
    * html, sólo para página de IPhone.
    * 
    * @param cantidadMinima
    * @param cantidadMaxima
    * @return
    */
   private String listadoDeCantidades(BigDecimal cantidadMinima, BigDecimal cantidadMaxima) {
      BigDecimal cincuenta = new BigDecimal(50);
      BigDecimal cero = new BigDecimal(0);
      cantidadMaxima = (cantidadMaxima.compareTo(cincuenta) > 0 ? new BigDecimal(50) : cantidadMaxima);
      String html = (String) cantidadesCalculadas.get(cantidadMinima.toString() + cantidadMaxima.toString());
      if (html != null) {
         return html;
      }

      //Que cantidadMinima sea cero no debería ocurrir, pero por si acaso
      BigDecimal b = (cantidadMinima.compareTo(cero) <= 0 ? new BigDecimal(1) : cantidadMinima);

      html = "";
      do {
         String sCantidad = Formatos.cantidadFO(b);
         html += "<option value=\"" + sCantidad + "\">" + sCantidad + "</option>";
         b = b.add(cantidadMinima);
      } while (b.compareTo(cantidadMaxima) <= 0);

      cantidadesCalculadas.put(cantidadMinima.toString() + cantidadMaxima.toString(), html);
      return html;
   }

   /**
    * @param palabra
    * @return
    * @throws IOException
    */
   private String[] ortografia(String palabra) throws IOException {
      palabra = palabra.toLowerCase();

      SpellChecker spellChecker = Ortografia.getInstance().getSpellChecker();
      /*
       * if (spellChecker.exist(palabra)) { //no hay falta de ortografía return
       * null; }
       */

      long ini = System.currentTimeMillis();
      String[] resultado = spellChecker.suggestSimilar(palabra, 5);//, qp.get,
      // "producto",
      // false);
      getLogger().debug("tiempo ortografia: " + (System.currentTimeMillis() - ini));

      long start = new Date().getTime();
      Directory fsDir = null;
      IndexSearcher is = null;
      try {
         fsDir = FSDirectory.getDirectory(indexDir);
         is = new IndexSearcher(fsDir);
         QueryParser qp = new QueryParser("producto", new SpanishAnalyzer(Sinonimos.getInstance().getSinominoMap()));
         qp.setDefaultOperator(QueryParser.AND_OPERATOR);
         long end = new Date().getTime();
         getLogger().debug("tiempo ortografia abrir indice lucene: " + (end - start));

         long start1 = new Date().getTime();
         List lista = new ArrayList();
         try {
            for (int i = 0; i < resultado.length; i++) {
               Query query = qp.parse(resultado[i]);
               TopDocCollector collector = new TopDocCollector(1);
               is.search(query, collector);
               ScoreDoc[] hits = collector.topDocs().scoreDocs;
               getLogger().debug("cantidad: " + resultado[i] + "   " + hits.length);
               if (hits.length > 0)
                  lista.add(resultado[i]);
            }
         } catch (Exception e) {
            getLogger().error("Error al buscar en ortografia: " + e.getMessage());
         }

         long end1 = new Date().getTime();
         getLogger().debug("tiempo ortografia busqueda prod: " + (end1 - start1));
         return (String[]) lista.toArray(new String[lista.size()]);
      } finally {
         if (is != null)
            is.close();
         if (fsDir != null)
            fsDir.close();
      }
   }

   /**
    * Para crear el select de la cantiodad de productos a mostrar
    * 
    * @param total
    * @param filaCantidad
    * @return
    */
   private List selectMostrarCantidad(int total, int filaCantidad) {
      if (filaCantidad > total)
         filaCantidad = total;
      List lista = new ArrayList();
      for (int i = 0; i < total; i += 10) {
         IValueSet cantidad = new ValueSet();
         int numero = (i + 10 <= total ? i + 10 : total);
         cantidad.setVariable("{id}", numero + "");
         cantidad.setVariable("{value}", (numero == total ? "Todos" : numero + ""));
         cantidad.setVariable("{selected}", (numero == filaCantidad ? "selected" : ""));
         lista.add(cantidad);
      }
      return lista;
   }

   /**
    * Para crear el select de OrdernarPor
    * 
    * @param selected
    * @return
    */
   private List selectOrdenarPor(String selected) {
      String[] ids = { "default", "nombre", "precio", "precio_por_unidad" };
      String[] values = { "Selecciona", "Nombre", "Precio", "Precio por unidad" };
      List lista = new ArrayList();
      for (int i = 0; i < ids.length; i++) {
         IValueSet ordenarPor = new ValueSet();
         ordenarPor.setVariable("{id}", ids[i]);
         ordenarPor.setVariable("{value}", values[i]);
         ordenarPor.setVariable("{selected}", (ids[i].equals(selected) ? "selected" : ""));
         lista.add(ordenarPor);
      }
      return lista;
   }
}
