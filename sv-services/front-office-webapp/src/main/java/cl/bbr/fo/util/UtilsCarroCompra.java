package cl.bbr.fo.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import net.sf.fastm.IValueSet;
import net.sf.fastm.ValueSet;
import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.SystemException;
import cl.bbr.jumbocl.clientes.dto.MiCarroDTO;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.jumbocl.productos.dto.CategoriaDTO;
import cl.bbr.log.Logging;
import cl.bbr.promo.lib.dto.ProductoPromosDTO;
import cl.bbr.promo.lib.dto.PromocionDTO;
import cl.bbr.promo.lib.dto.doRecalculoCriterio;
import cl.bbr.promo.lib.dto.doRecalculoResultado;

public class UtilsCarroCompra {
    private int contador;
    private Logging logger;
    
   //private static UtilsCarroCompra utilsCarroCompra = null;
	
	/*public static UtilsCarroCompra getUtilsCarroCompra() {

		if (utilsCarroCompra == null) {
			utilsCarroCompra = new UtilsCarroCompra();
		}
		return utilsCarroCompra;
	}*/
	public UtilsCarroCompra() {
		// TODO Auto-generated constructor stub
		this.contador = 0;
		logger = new Logging(this);
	}
	
	/**
	    * 
	    * @param o
	    * @return
	    */
		public synchronized List sort(List o) {
			// TODO Auto-generated method stub
			List result = new ArrayList();
			try{
				//Collections col = (Collections) o;
				Collections.sort(o);
				for (Iterator iterator = o.iterator(); iterator.hasNext();) {
					Object object = iterator.next();
					if(object != null && object instanceof MiCarroDTO){
						MiCarroDTO dto = (MiCarroDTO) object;
						result.add(dto);
					} 
				}
			}catch (Exception e) {
				logger.error("Error UtilsCarroCompra.sort : " + e);
			}
			return o;
		}
		
		 /**
	     * 
	     * @param lista
	     * @return
	     */
		public synchronized List setStyleFila(List lista) {
			// TODO Auto-generated method stub
			//int value = 0;
		  	//String obj = String.valueOf(carr_id);
		 	try{
				for (int i = 0; i < lista.size(); i++) {
					 IValueSet o = (IValueSet) lista.get(i);
					if(o != null ){
						logger.info("element:"+o);
						o.setVariable("{style_fila}", " ");
						lista.set(i, o);
					}
				}
		 	}catch (Exception e) {
				// TODO: handle exception
		 		logger.error("Error UtilsCarroCompra.setStyleFila : " + e);
			}
			return lista;
		}
		
		
		/**
		 * rescata idProducto de lista
		 * 
		 * @param idsProductos
		 * @return
		 */
		public synchronized String getProducto(List idsProductos) {
			String id = null;

			for (int i = 0; i < idsProductos.size(); i++) {
				if (idsProductos.get(i) != null) {
					id = idsProductos.get(i).toString();
					break;
				}
			}
			return id;
		}
		
		
		/**
		 * 
		 * @param top
		 * @return
		 */
		public synchronized int cuentaLista(IValueSet top) {
			int contador = 0;
			try {
				List arrayList = null;
				if (top != null)
					arrayList = (List) top.getDynamicValueSets("lista_carro");
				if (arrayList != null)
					contador = arrayList.size();
			} catch (Exception e) {
				logger.error("Error UtilsCarroCompra.cuentaLista : " + e);
			}
			return contador;
		}
		
		
		
		/**
		 * all list 
		 * @param primerProdNuevo
		 * @param idProductoAgregado
		 * @param idsProductos
		 * @param rb
		 * @param listaCarro
		 * @param top
		 * @return
		 */
		public synchronized IValueSet miCarroDTO(boolean primerProdNuevo, List idsProductos,
				ResourceBundle rb, List listaCarro, IValueSet top) {
			List datos_e = new ArrayList();
			long total = 0;
			long valorid = 0;
			long precio_total = 0;
			long idProductoAgregado = 0;
			try {
				for (int i = 0; i < listaCarro.size(); i++) {
					MiCarroDTO car = (MiCarroDTO) listaCarro.get(i);
					IValueSet fila = new ValueSet();
					fila.setVariable("{nota}", car.getNota());
					fila.setVariable("{id}", car.getId() + "");
					fila.setVariable("{pro_id}", car.getPro_id() + "");
					fila.setVariable("{id_intermedia}", car.getIdIntermedia() + "");
					fila.setVariable("{nom_intermedia}", car.getNombreIntermedia() + "");
					fila.setVariable("{id_terminal}", car.getIdTerminal() + "");
					fila.setVariable("{nom_terminal}", car.getNombreTerminal() + "");
					valorid = car.getId();
					precio_total = Utils.redondearFO(car.getCantidad() * car.getPrecio());
					if (car.tieneStock()) {
						total += precio_total;
					}
					String nombre_pro = (car.getTipo_producto() + " " + car.getNom_marca()).trim();
					// se separan los '/' de los productos q contengan este caracter
					nombre_pro = cl.bbr.jumbocl.common.utils.Utils.separarDescripcionesLargas(nombre_pro);
					int largo_pro = Integer.parseInt(rb.getString("orderitemdisplay.largonombreproducto"));
					if (nombre_pro.length() < largo_pro)
						largo_pro = nombre_pro.length();
					fila.setVariable("{nombrepro}",nombre_pro.substring(0, largo_pro));
					fila.setVariable("{nommarca}", car.getTipo_producto() + "\n"+ car.getNombre() + "\n" + car.getNom_marca() + "");
					fila.setVariable("{subtotal}",Formatos.formatoPrecioFO(precio_total) + "");
					fila.setVariable("{i}", i + "");
					// Si el producto es con seleccion
					if (car.getUnidad_tipo().charAt(0) == 'S') {
						fila.setDynamicValueSets("LISTA_SEL",addLista(valorid, car.getPro_id(), i,car.getInter_maximo(),car.getInter_valor(), car.getTipre(),
										car.getCantidad()));
					} else {
						fila.setDynamicValueSets("INPUT_SEL",addLista(valorid, car.getPro_id(), i,car.getInter_maximo(),car.getInter_valor(), car.getTipre(),
										car.getCantidad()));
					}

					fila.setVariable("{id}", valorid + "");
					fila.setVariable("{contador}", i + "");

					if (esNuevoProducto(idsProductos, car.getPro_id())) {
						fila.setVariable("{style_fila}", "productoAgregado");
						if (primerProdNuevo) {
							idProductoAgregado = Long.parseLong(car.getPro_id());
							primerProdNuevo = false;
						}
					} else {
						fila.setVariable("{style_fila}", " ");
					}
					datos_e.add(fila);
				}
				top.setDynamicValueSets("lista_carro", datos_e);
				top.setVariable("total_carro", Long.toString(total));
				top.setVariable("{id_prod_add}", idProductoAgregado + "");
				
				if (datos_e.size() == 0) {
					IValueSet fila_lista_sel = new ValueSet();
					fila_lista_sel.setVariable("p", "");
					List datos_p = new ArrayList();
					datos_p.add(fila_lista_sel);
					top.setDynamicValueSets("lista_carro_img", datos_p);
				}
			} catch (Exception e) {
				//logger.info("Error UtilsCarroCompra.miCarroDTO : "+e.getMessage());
				logger.error("Error UtilsCarroCompra.miCarroDTO : " + e);
			}
			return top;
		}
		
		
		 /**
	     * 
	     * @param listaCarro
	     * @param dto
	     * @return
	     */
		public synchronized List addLista(List itemCarro, List listaCarro) {
			// TODO Auto-generated method stub
			List o = new ArrayList();
			try{
	 			if(listaCarro != null){
					for (int x = 0; x < listaCarro.size(); x++) {
						MiCarroDTO element = (MiCarroDTO) listaCarro.get(x);
						o.add(element);
					}
				}
				if(itemCarro != null){
					for (int i = 0; i < itemCarro.size(); i++) {
						MiCarroDTO element = (MiCarroDTO) itemCarro.get(i);
						/*buscar posicion si es update*/
						if(o.indexOf(element) == -1){
						     o.add(element);
						}else{
							int index = o.indexOf(element);
							o.remove(index);
							o.add(element);
						}
					}
				}
				
			}catch (Exception e) {
				// TODO: handle exception
				logger.error("Error UtilsCarroCompra. addLista MiCarroDTO: " + e.getMessage());
				
			}
			return o;
		}
		
		/**
		 * 
		 * @param idsProductos
		 * @param pro_id
		 * @return
		 */
		public synchronized boolean esNuevoProducto(List idsProductos, String pro_id) {
			for (int i = 0; i < idsProductos.size(); i++) {
				if (idsProductos.get(i).equals(pro_id)) {
					return true;
				}
			}
			return false;
		}
		
		
		/**
		 * 
		 * @param valorid
		 * @param pro_id
		 * @param i
		 * @param inter_maximo
		 * @param inter_valor
		 * @param tipre
		 * @param cantidad
		 * @return
		 */
		public synchronized List addLista(long valorid, String pro_id, int i,
				double inter_maximo, double inter_valor, String tipre,
				double cantidad) {
			List aux_blanco = new ArrayList();
			IValueSet fila_lista_sel = new ValueSet();
			fila_lista_sel.setVariable("{id}", valorid + "");
			fila_lista_sel.setVariable("{pro_id}", pro_id + "");
			fila_lista_sel.setVariable("{contador}", i + "");
			fila_lista_sel.setVariable("{maximo}", inter_maximo + "");
			fila_lista_sel.setVariable("{intervalo}", inter_valor + "");
			fila_lista_sel.setVariable("{unidad}", tipre + "");
			fila_lista_sel.setVariable("{valor}",
					Formatos.formatoIntervaloFO(cantidad) + "");

			aux_blanco.add(fila_lista_sel);

			return aux_blanco;
		}
		
		
		/**
		 * 
		 * @param list_categoria
		 * @param listaTop
		 * @param rb 
		 * @param idsProductos 
		 * @param primerProdNuevo 
		 * @param ses_cli_rut 
		 * @param ses_loc_id 
		 * @param ses_colaborador 
		 * @param listaCarro 
		 * @param biz 
		 * @param l_torec 
		 * @return
		 */
		public synchronized  IValueSet reAgrupaCategorias(List list_categoria, List listaTop, IValueSet top, List dto){
			
			List listaCat = new ArrayList();
			List newListaCarro = new ArrayList();
			CategoriaDTO cat1 = null;
			 
			try{
				if( (listaTop != null && listaTop.size() > 0) && list_categoria != null){
		           for (int w = 0; w < list_categoria.size(); w++) {
		        	   cat1 = (CategoriaDTO) list_categoria.get(w);
		        	   IValueSet nivel1 = new ValueSet();
		        	   newListaCarro = new ArrayList();
		        	    nivel1.setVariable("{cat_id}",  cat1.getId()+"");   
			            nivel1.setVariable("{cat_nombre}", cat1.getNombre()+"");
			            nivel1.setVariable("{cat_descripcion}", cat1.getDescripcion()+"");
			            nivel1.setVariable("{cat_tipo}", cat1.getTipo()+"");
			            nivel1.setVariable("{cat_padre}", "0");
			            nivel1.setVariable("{numero}", w + "");
						 for(int i = 0; i < listaTop.size() ; i++ ) {
							 IValueSet element = (IValueSet) listaTop.get(i);
					            //Cabeceras
					         if(listaEqualCategoria(cat1,element)){
				                element.setVariable("{item_cat_id}", cat1.getId()+"");
				                element.setVariable("{item_cat_nombre}", cat1.getNombre()+"");
				                if(newListaCarro.indexOf(element)== -1)
				                   newListaCarro.add(element);
					        }/* fin del if listaEqualCategoria  */
						 }/* fin for listaCarro */
					     if(newListaCarro.size()> 0){
					    	//List listaSort = sortIValueSet(newListaCarro, dto); // no es necesario re ordenar
						    nivel1.setDynamicValueSets("lista_carro", newListaCarro);
						    listaCat.add(nivel1);
					     }
					  } /*fin for de list_categoria */
		            //top.setDynamicValueSets("lista_carro", listaTop);
		           top.setDynamicValueSets("CATEGORIAS", listaCat);
				}else if(listaTop.size() == 0){/* Fin if listaCarro  */
					if(top != null){
						if(top.getDynamicValueSets("CATEGORIAS")!= null && top.getDynamicValueSets("CATEGORIAS").size()>0){
							top.getDynamicValueSets("CATEGORIAS").clear();
						}
					}
				}
			}catch (Exception e) {
				// TODO: handle exception
				logger.error("Error UtilsCarroCompra. en carga Categorias :"+e);
			} 
		    
		   
		    return top;
		   
		}
		
 	
   public synchronized IValueSet agrupaCategorias(List list_categoria, IValueSet top,
			List listaCarroDto, ResourceBundle rb, List idsProductos,
			boolean primerProdNuevo, long total, String ses_loc_id,
			String ses_colaborador, String ses_cli_rut,
			List listaCarro2, BizDelegate biz, List l_torec){
	 		List listaCat = new ArrayList();
			List newListaCarro = new ArrayList();
			List endListaCarro = new ArrayList();
			CategoriaDTO cat1 = null;
			boolean newProduct = false;
			
			//Mapa que almacena productos sin problemas de categoria
			Map productosConCategoria = new HashMap();
			//Mapa que almacena productos con problemas de categoria
			Map productosSinCategoria = new HashMap();
			
			try{
				if( listaCarroDto != null ){
				String idProducto = getProducto(idsProductos);
				if(idProducto != null){
					newProduct = true;
				}else idProducto= "0";
				 if((listaCarroDto != null && listaCarroDto.size() > 0) && list_categoria != null){
		           for (int w = 0; w < list_categoria.size(); w++) {
		        	   cat1 = (CategoriaDTO) list_categoria.get(w);
		        	   
		        	   newListaCarro = new ArrayList();
		        	    
						 for(int i = 0; i < listaCarroDto.size() ; i++ ) {
							 MiCarroDTO element = (MiCarroDTO) listaCarroDto.get(i);
							 
							 if(productosConCategoria.get(new Long(element.getId())) == null)
							 {
								 productosSinCategoria.put(new Long(element.getId()),element);
						            //Cabeceras
						         if(listaEqualCategoria(cat1,element)){
						        	 productosSinCategoria.remove(new Long(element.getId()));
						        	 productosConCategoria.put(new Long(element.getId()),element);
					                element.setId_cabecera(cat1.getId());//element.setVariable("{item_cat_id}", cat1.getId()+"");
					                element.setNombre_cabecera(cat1.getNombre());//element.setVariable("{item_cat_nombre}", cat1.getNombre()+"");
					                //if(newListaCarro.indexOf(element)== -1){
					                newListaCarro.add(element);
					                endListaCarro.add(element);
					                //}
					                logger.info("Categoria."+cat1.getNombre()+"..new item carro:" + element.getTipo_producto() + "." +element.getNombre()+ ".contador."+contador);
						        }/* fin del if listaEqualCategoria */
							 }
							 
						 }/* fin for listaCarro*/
					     if(newListaCarro.size()> 0){
					    	 IValueSet nivel1 = new ValueSet();
					    	 nivel1.setVariable("{cat_id}",  cat1.getId()+"");   
					         nivel1.setVariable("{cat_nombre}", cat1.getNombre()+"");
					         nivel1.setVariable("{cat_tipo}", cat1.getTipo()+"");
					         nivel1.setVariable("{cat_descripcion}", cat1.getDescripcion()+"");
					         nivel1.setVariable("{cat_padre}", "0");
					         nivel1.setVariable("{numero}", w + "");
					         //String cat_id, String numero, String cat_nombre
					         nivel1.setVariable("{link_categoria}",generaLinkCategoria((String)nivel1.getVariable("{cat_id}"),(String)nivel1.getVariable("{numero}"),(String)nivel1.getVariable("{cat_nombre}")));
					    	logger.info("element carro: Categoria"+ cat1.getNombre()+"..posicion.endListaCarro."+ endListaCarro.size()+".contador."+contador + ".posicion.newListaCarro."+newListaCarro.size());
					    	//List listaSort = sort(newListaCarro); 
					    	Collections.sort(newListaCarro);
					    	List listaSet = addItemList(newProduct, newListaCarro, top, rb, idsProductos, Long.parseLong(idProducto), contador);
					    	contador ++;
					    	if( endListaCarro.size() > contador){ 
					    		contador = endListaCarro.size() ;
					    	}else if(endListaCarro.size() < contador ){
					    		contador = endListaCarro.size() - 1;
					    	}
					    	nivel1.setDynamicValueSets("lista_carro", listaSet);
						    listaCat.add(nivel1);
					     }
					  } /*fin for de list_categoria*/
		           
		           	if(productosSinCategoria.size() > 0)
					{
						agregaProductosSinCategoriaActiva(new ArrayList(productosSinCategoria.values()), listaCat, newProduct, top, rb, idsProductos, idProducto, endListaCarro);
					}
		           	
		            if(listaCat != null && listaCat.size() > 0){
				    	if(idsProductos.size() > 0){
				    		top = addItemCarro(newProduct, listaCarroDto, top, rb, idsProductos, Long.parseLong(idProducto));
				    		total = Long.parseLong(top.getVariable("total_carro").toString());
				    	}else{
				    	    top = miCarroDTO(primerProdNuevo, idsProductos, rb, endListaCarro,	top);
				    	    total = Long.parseLong(top.getVariable("total_carro").toString());
				    	}
			    		top = getPromoMiCarro(rb, l_torec, endListaCarro, biz, top, total, ses_loc_id, ses_colaborador, ses_cli_rut);
				    	/*  * 
			            //top.setDynamicValueSets("lista_carro", listaTop);
					    if(top.getDynamicValueSets("CATEGORIAS")!= null){
					       top.getDynamicValueSets("CATEGORIAS").clear();
					    } */
			            top.setDynamicValueSets("CATEGORIAS", listaCat);
			    	}
		           /*fin if de listaCarroDTO*/
				  }else if(listaCarroDto != null && listaCarroDto.size() == 0){
					  long value = 0;
					  if(top != null){
							top.setVariable("{total_desc_tc}", Formatos.formatoPrecioFO(value));
							top.setVariable("{total_desc_tc_sf}", Formatos.formatoPrecioFO(value));
							top.setVariable("{total_desc}", Formatos.formatoPrecioFO(value));
							top.setVariable("{total_desc_sf}", Formatos.formatoPrecioFO(value));
							top.setVariable("{promo_desc}", String.valueOf(value));
							top.setVariable("{total}", Formatos.formatoPrecioFO(value) + "");
							top.setVariable("{cant_reg}", 0 + "");
							top.setVariable("total_carro", Long.toString(value));
					  }else{
						  top = new ValueSet();
						  }
						//top = getPromoMiCarro(rb, l_torec, listaCarroDto, biz, top, total, ses_loc_id, ses_colaborador, ses_cli_rut);
					  }
				  
				}/* Fin if listaCarro */
			}catch (Exception e) {
				// TODO: handle exception
				logger.error("Error UtilsCarroCompra. en carga Categorias :"+e);
				//logger.info("Error UtilsCarroCompra.agrupaCategorias en carga Categorias :{"+e+"}");
			} 
		    return top;
		   
		}
		
		 public synchronized List sortIValueSet(List newListaCarro, List dto) {
			// TODO Auto-generated method stub
			List lista = new ArrayList();
			List result1 = new ArrayList();
			List result2 = new ArrayList();
			try{
				if(newListaCarro != null){
					for (int i = 0; i < newListaCarro.size(); i++) {
						IValueSet element = (IValueSet) newListaCarro.get(i);
						if(element != null){
							MiCarroDTO input = new MiCarroDTO();
							input = getFilaMiCarro(element, dto);
							lista.add(input);
						}
					}
				}
				result1 = sort(lista);
				if(newListaCarro != null){
					for (int i = 0; i < result1.size(); i++) {
						MiCarroDTO element = (MiCarroDTO) result1.get(i);
						if(element != null){
							IValueSet output = new ValueSet();
							output = getFilaMiCarro(element);
							
							result2.add(output) ;
						}
					}
				}
			}catch (Exception e) {
				// TODO: handle exception
				logger.error("Error UtilsCarroCompra.sortIValueSet : " + e);
			}
			return result2;
		}
        
		public synchronized IValueSet getFilaMiCarro(MiCarroDTO element) {
			// TODO Auto-generated method stub
			IValueSet o = new ValueSet();
			try{
				if(element != null){
					o.setVariable("{nom_intermedia}",element.getNombreIntermedia());
					o.setVariable("{nombrepro}",element.getTipo_producto());
					o.setVariable("{item_cat_id}",element.getNombre_cabecera());//element.setVariable("{item_cat_id}", cat1.getId()+"");
					o.setVariable("{item_cat_nombre}",String.valueOf(element.getId_cabecera())); //element.setVariable("{item_cat_nombre}", cat1.getNombre()+"");
		            
					o.setVariable("{id}",String.valueOf(element.getId()));
					o.setVariable("{pro_id}",element.getPro_id());
					o.setVariable("{nommarca}",element.getNom_marca()); //fila.setVariable("{nommarca}", car.getTipo_producto() + "\n"+ car.getNombre() + "\n" + car.getNom_marca() + "");
					o.setVariable("{subtotal}",String.valueOf(Formatos.formatoPrecioFO(element.getPrecio(), true))); //fila.setVariable("{subtotal}",Formatos.formatoPrecio(precio_total) + "");
					
				    //  token ","
					String descpcion = element.getDescripcion();
					String [] campos = descpcion.split(",");
					if(!campos[0].equals("#"))
					  o.setVariable("{contador}",campos[0]);
					if(!campos[1].equals("#"))
					  o.setVariable("{maximo}",campos[1]);
					if(!campos[2].equals("#"))
					  o.setVariable("{intervalo}",campos[2]);
					if(!campos[3].equals("#"))
					 o.setVariable("{unidad}",campos[3]);
					if(!campos[4].equals("#"))
					  o.setVariable("{valor}",campos[4]);
					if(!campos[5].equals("#"))
					  o.setVariable("{style_fila}",campos[5]);
					if(!campos[6].equals("#"))
					  o.setVariable("{contador}" ,campos[6]);
					if(!campos[7].equals("#"))
					  o.setVariable("{nota}",campos[7]);	//fila.setVariable("{nota}", car.getNota());
					if(!campos[8].equals("#"))
					  o.setVariable("{id}",campos[8]);	//fila.setVariable("{id}", car.getId() + "");
					if(!campos[9].equals("#"))
					  o.setVariable("{pro_id}",campos[9]);	//fila.setVariable("{pro_id}", car.getPro_id() + "");
					if(!campos[10].equals("#"))
					  o.setVariable("{id_intermedia}",campos[10]);	//fila.setVariable("{id_intermedia}", car.getIdIntermedia() + "");
					if(!campos[11].equals("#"))
					  o.setVariable("{nom_intermedia}",campos[11]);	//fila.setVariable("{nom_intermedia}", car.getNombreIntermedia() + "");
					if(!campos[12].equals("#"))
					  o.setVariable("{id_terminal}",campos[12]);	//fila.setVariable("{id_terminal}", car.getIdTerminal() + "");
					if(!campos[13].equals("#"))
					  o.setVariable("{nom_terminal}",campos[13]);	//fila.setVariable("{nom_terminal}", car.getNombreTerminal() + "");
					if(!campos[14].equals("#"))
					  o.setVariable("{i}",campos[14]); //fila.setVariable("{i}", i + "");*/
					if (element.getUnidad_tipo().charAt(0) == 'S') {
						o.setDynamicValueSets("LISTA_SEL",addLista(element.getId(), element.getPro_id(), Integer.parseInt(campos[14]),element.getInter_maximo(),element.getInter_valor(), element.getTipre(),
										element.getCantidad()));
					} else {
						o.setDynamicValueSets("INPUT_SEL",addLista(element.getId(), element.getPro_id(), Integer.parseInt(campos[14]),element.getInter_maximo(),element.getInter_valor(), element.getTipre(),
										element.getCantidad()));
					}
				}
			}catch (Exception e) {
				// TODO: handle exception
				 logger.error("Error UtilsCarroCompra.getFilaMiCarro : " + e);
			}
			return o;
		}
        /**
         * 
         * @param element
         * @param dto
         * @return
         */
		public synchronized MiCarroDTO getFilaMiCarro(IValueSet element, List dto) {
			MiCarroDTO o = new MiCarroDTO();
			try{
				if(element != null){
					o = buscarObject(element, dto);
					o.setNombreIntermedia(element.getVariable("{nom_intermedia}").toString());
					o.setTipo_producto(element.getVariable("{nombrepro}").toString());
					o.setNombre_cabecera(element.getVariable("{item_cat_nombre}").toString());//element.setVariable("{item_cat_id}", cat1.getId()+"");
					o.setId_cabecera(Long.parseLong(element.getVariable("{item_cat_id}").toString())); //element.setVariable("{item_cat_nombre}", cat1.getNombre()+"");
		            
					o.setId(Long.parseLong(element.getVariable("{id}").toString()));
					o.setPro_id(element.getVariable("{pro_id}").toString());
					o.setNom_marca(element.getVariable("{nommarca}").toString()); //fila.setVariable("{nommarca}", car.getTipo_producto() + "\n"+ car.getNombre() + "\n" + car.getNom_marca() + "");
					o.setPrecio(Formatos.unFormatoPrecioFO(element.getVariable("{subtotal}").toString())); //fila.setVariable("{subtotal}",Formatos.formatoPrecio(precio_total) + "");
					
					//  token ","
					String token = "";
					
						if(element.getVariable("{contador}") != null)
								  token += element.getVariable("{contador}").toString() + "," ;
						else token += "#,";
						if(element.getVariable("{maximo}") != null)
							token += element.getVariable("{maximo}").toString() + ",";
						else token += "#,";
						if(element.getVariable("{intervalo}")!= null)
							token += element.getVariable("{intervalo}").toString() + ",";
						else token += "#,";
						if(element.getVariable("{unidad}")!= null)
							token += element.getVariable("{unidad}").toString() + ",";
						else token += "#,";
					    if(element.getVariable("{valor}")!= null)
					    	token += element.getVariable("{valor}").toString() + ",";
					    else token += "#,";
					    if(element.getVariable("{style_fila}")!= null)
					    	token += element.getVariable("{style_fila}").toString() + ",";
					    else token += "#,";
					    if(element.getVariable("{contador}")!= null)
					    	token += element.getVariable("{contador}" ).toString() + ",";
					    else token += "#,";
					    if(element.getVariable("{nota}") != null)
					    	token += element.getVariable("{nota}").toString() + ",";	//fila.setVariable("{nota}", car.getNota());
					    else token += "#,";
					    if(element.getVariable("{id}")!= null)
					    	token += element.getVariable("{id}").toString() + ",";	//fila.setVariable("{id}", car.getId() + "");
					    else token += "#,";
					    if(element.getVariable("{pro_id}") != null)
					    	token += element.getVariable("{pro_id}").toString() + ",";	//fila.setVariable("{pro_id}", car.getPro_id() + "");
					    else token += "#,";
					    if(element.getVariable("{id_intermedia}") != null)
					        token += element.getVariable("{id_intermedia}").toString() + ",";	//fila.setVariable("{id_intermedia}", car.getIdIntermedia() + "");
					    else token += "#,";
					    if(element.getVariable("{nom_intermedia}")!= null)
					    	token += element.getVariable("{nom_intermedia}").toString() + ",";	//fila.setVariable("{nom_intermedia}", car.getNombreIntermedia() + "");
					    else token += "#,";
					    if(element.getVariable("{id_terminal}") != null)
					    	token += element.getVariable("{id_terminal}").toString() + ",";	//fila.setVariable("{id_terminal}", car.getIdTerminal() + "");
					    else token += "#,";
					    if(element.getVariable("{nom_terminal}") != null)
					    	token += element.getVariable("{nom_terminal}").toString() + ",";	//fila.setVariable("{nom_terminal}", car.getNombreTerminal() + "");
					    else token += "#,";
					    if(element.getVariable("{i}")!= null)
					    	token += element.getVariable("{i}")  ; //fila.setVariable("{i}", i + "");
					    else token += "#,";
					    o.setDescripcion(token);
				}
			}catch (Exception e) {
				// TODO: handle exception
				 logger.error("Error UtilsCarroCompra.getFilaMiCarro : " + e);
			}
			return o;
		}

		public synchronized MiCarroDTO buscarObject(IValueSet element, List dto) {
			// TODO Auto-generated method stub
			MiCarroDTO obj = new MiCarroDTO();
			try{
				for (int i = 0; i < dto.size(); i++) {
					MiCarroDTO o = (MiCarroDTO) dto.get(i);
					if(element.getVariable("{id_intermedia}").equals(String.valueOf(o.getIdIntermedia()))
							&& element.getVariable("{id}").toString().equals(String.valueOf(o.getId()))){
						obj = o; 
						break;
					}
				}
			}catch (Exception e) {
				// TODO: handle exception
				 logger.error("Error UtilsCarroCompra.buscarObject : " + e);
			}
			return obj;
		}

		/**
		    * 
		    * @param cat1
		    * @param itemCarro
		    * @return
		    */
		   public synchronized  boolean listaEqualCategoria(CategoriaDTO cat1, IValueSet itemCarro) {
			   boolean sw = false;
			   try{
				   if(itemCarro != null && cat1 != null){
						 List intermedias = cat1.getCategorias();
						 bucle1:
						 for (int j = 0; j < intermedias.size(); j++) {
							 CategoriaDTO cat2 = (CategoriaDTO)intermedias.get(j);
							 if(itemCarro.getVariable("{id_intermedia}").equals(String.valueOf(cat2.getId()))
									&& cat2.getId_padre()== cat1.getId() ){
								 List terminales = cat2.getCategorias();
								 for (int  k=0; k< terminales.size(); k++){
									    CategoriaDTO cat3 = (CategoriaDTO)terminales.get(k);
				                        String id_term = (String) itemCarro.getVariable("{id_terminal}");
				                        if (id_term.equals(String.valueOf(cat3.getId()))){ ///cat2.getId() == cat3.getId_padre()  
				                        	sw = true;
				   						    break bucle1;
				                        }
				                    }/*fin del for*/
							 }
							 if(sw) break;  /* no se usa borrar */
						 }/* fin del for bucle1*/
				   }/* fin del if*/
			   }catch (Exception e) {
				// TODO: handle exception
				   logger.error("Error UtilsCarroCompra.listaEqualCategoria : " + e);
			   }
			return sw;
			}
		   
		   /**
		    * 
		    * @param cat1
		    * @param itemCarro
		    * @return
		    */
		   public synchronized boolean listaEqualCategoria(CategoriaDTO cat1, MiCarroDTO itemCarro) {
			   boolean sw = false;
			   try{
				   if(itemCarro != null && cat1 != null){
						 List intermedias = cat1.getCategorias();
						 bucle1:
						 for (int j = 0; j < intermedias.size(); j++) {
							 CategoriaDTO cat2 = (CategoriaDTO)intermedias.get(j);
							 if(itemCarro.getIdIntermedia()==((cat2.getId()))
									 ){
								 if(cat2.getId_padre()== cat1.getId()){
								 List terminales = cat2.getCategorias();
								 for (int  k=0; k< terminales.size(); k++){
									    CategoriaDTO cat3 = (CategoriaDTO)terminales.get(k);
				                        String id_term = String.valueOf(itemCarro.getIdTerminal());
				                        if (id_term.equals(String.valueOf(cat3.getId()))){ //cat2.getId() == cat3.getId_padre() && 
				                        	sw = true;
				   						    break bucle1;
				                        }
				                    }/*fin del for*/
								 }
							 }
							 if(sw) break;  /* no se usa borrar */
						 }/* fin del for bucle1*/
				   }/* fin del if*/
			   }catch (Exception e) {
				// TODO: handle exception
				   logger.error("Error UtilsCarroCompra.listaEqualCategoria : " + e);
				   logger.debug("Error UtilsCarroCompra.listaEqualCategoria : " + e);
			   }
			return sw;
			}
		   

			
			
			/**
			 * Agrega al final de la lista del carro nuevo item producto
			 * 
			 * @param listaCarro
			 * @param top
			 * @param rb
			 * @param list_categoria 
			 * @return
			 */
			public synchronized IValueSet addItemCarro(boolean primerProdNuevo, List listaCarro, IValueSet top,
					ResourceBundle rb, List idsProductos, long idProductoAgregado) {
				List datos_e = new ArrayList();
				UtilsCarroCompra util = new UtilsCarroCompra();
				long total = 0;
				long valorid = 0;
				long precio_total = 0;
				List categorias = new ArrayList();
				List newListaCarro = new ArrayList();
				CategoriaDTO cat1 = null;
				int items = util.cuentaLista(top);
				
				try {
		 
						if(listaCarro != null){
							for (int i = 0; i < listaCarro.size(); i++) {
								MiCarroDTO car = (MiCarroDTO) listaCarro.get(i);
							
								IValueSet fila = new ValueSet();
								fila.setVariable("{nota}", car.getNota());
								fila.setVariable("{id}", car.getId() + "");
								fila.setVariable("{pro_id}", car.getPro_id() + "");
								fila.setVariable("{id_intermedia}", car.getIdIntermedia() + "");
								fila.setVariable("{nom_intermedia}", car.getNombreIntermedia() + "");
								fila.setVariable("{id_terminal}", car.getIdTerminal() + "");
								fila.setVariable("{nom_terminal}", car.getNombreTerminal() + "");
								valorid = car.getId();
								precio_total = Utils.redondearFO(car.getCantidad()* car.getPrecio());
								if (car.tieneStock()) {
									total += precio_total;
									//top.setVariable("total_carro", Long.toString(total));
								}
								String nombre_pro = (car.getTipo_producto() + " " + car.getNom_marca()).trim();
								// se separan los '/' de los productos q contengan este caracter
								nombre_pro = cl.bbr.jumbocl.common.utils.Utils.separarDescripcionesLargas(nombre_pro);
								int largo_pro = Integer.parseInt(rb.getString("orderitemdisplay.largonombreproducto"));
								if (nombre_pro.length() < largo_pro)
									largo_pro = nombre_pro.length();
								fila.setVariable("{nombrepro}",nombre_pro.substring(0, largo_pro));
								fila.setVariable("{nommarca}", car.getTipo_producto() + "\n" + car.getNombre() + "\n" + car.getNom_marca() + "");
								fila.setVariable("{subtotal}", Formatos.formatoPrecioFO(precio_total) + "");
								fila.setVariable("{i}", i + "");
								// Si el producto es con seleccion
								if (car.getUnidad_tipo().charAt(0) == 'S') {
									fila.setDynamicValueSets("LISTA_SEL", util.addLista(valorid, car.getPro_id(), i, car.getInter_maximo(),car.getInter_valor(), car.getTipre(), car.getCantidad()));
								} else {
									fila.setDynamicValueSets("INPUT_SEL", util.addLista(valorid, car.getPro_id(), i,car.getInter_maximo(),car.getInter_valor(), car.getTipre(), car.getCantidad()));
								}
								fila.setVariable("{id}", valorid + "");
								fila.setVariable("{contador}", i + "");
				                if(car.getPro_id().equals(String.valueOf(idProductoAgregado))){
									fila.setVariable("{style_fila}", "productoAgregado" );
									fila.setVariable("{id_prod_add}", idProductoAgregado +"" );
				                }else{
				                	fila.setVariable("{style_fila}", " ");
				                }
						       datos_e.add(fila);
							}  //fin de for 
							top.setDynamicValueSets("lista_carro", datos_e);
							top.setVariable("total_carro", Long.toString(total));
							top.setVariable("{id_prod_add}", idProductoAgregado + "");
						}/* fin de lista null*/
					} catch (Exception e) {
					logger.error("Error UtilsCarroCompra.addItemCarro : " + e);
				}
				return top;
			}
			/**
			 * 
			 * @param primerProdNuevo
			 * @param listaCarro
			 * @param top
			 * @param rb
			 * @param idsProductos
			 * @param idProductoAgregado
			 * @return
			 */
			public synchronized List addItemList(boolean primerProdNuevo, List listaCarro, IValueSet top,
					ResourceBundle rb, List idsProductos, long idProductoAgregado, int index) {
				List datos_e = new ArrayList();
				UtilsCarroCompra util = new UtilsCarroCompra();
				long valorid = 0;
				long precio_total = 0;
				int items = index  ;//util.cuentaLista(top);
				
				try {
		 
						if(listaCarro != null){
							for (int i = 0; i < listaCarro.size(); i++) {
								MiCarroDTO car = (MiCarroDTO) listaCarro.get(i);
								IValueSet fila = new ValueSet();
								fila.setVariable("{nota}", car.getNota());
								fila.setVariable("{id}", car.getId() + "");
								fila.setVariable("{pro_id}", car.getPro_id() + "");
								fila.setVariable("{id_intermedia}", car.getIdIntermedia() + "");
								fila.setVariable("{nom_intermedia}", car.getNombreIntermedia() + "");
								fila.setVariable("{id_terminal}", car.getIdTerminal() + "");
								fila.setVariable("{nom_terminal}", car.getNombreTerminal() + "");
								valorid = car.getId();
								precio_total = Utils.redondearFO(car.getCantidad()* car.getPrecio());
								if (car.tieneStock()) {
								}
								String nombre_pro = (car.getTipo_producto() + " " + car.getNom_marca()).trim();
								// se separan los '/' de los productos q contengan este caracter
								nombre_pro = cl.bbr.jumbocl.common.utils.Utils.separarDescripcionesLargas(nombre_pro);
								int largo_pro = Integer.parseInt(rb.getString("orderitemdisplay.largonombreproducto"));
								if (nombre_pro.length() < largo_pro)
									largo_pro = nombre_pro.length();
								fila.setVariable("{nombrepro}",nombre_pro.substring(0, largo_pro));
								fila.setVariable("{nommarca}", car.getTipo_producto() + "\n" + car.getNombre() + "\n" + car.getNom_marca() + "");
								fila.setVariable("{subtotal}", Formatos.formatoPrecioFO(precio_total) + "");
								fila.setVariable("{i}", items + "");
								// Si el producto es con seleccion
								if (car.getUnidad_tipo().charAt(0) == 'S') {
									fila.setDynamicValueSets("LISTA_SEL", util.addLista(valorid, car.getPro_id(), items, car.getInter_maximo(),car.getInter_valor(), car.getTipre(), car.getCantidad()));
								} else {
									fila.setDynamicValueSets("INPUT_SEL", util.addLista(valorid, car.getPro_id(), items ,car.getInter_maximo(),car.getInter_valor(), car.getTipre(), car.getCantidad()));
								}
								fila.setVariable("{id}", valorid + "");
								fila.setVariable("{contador}", items + "");
				                if(car.getPro_id().equals(String.valueOf(idProductoAgregado))){
									fila.setVariable("{style_fila}", "productoAgregado" );
									fila.setVariable("{id_prod_add}", idProductoAgregado +"" );
				                }else{
				                	fila.setVariable("{style_fila}", " ");
				                }
						       datos_e.add(fila);
						       items++;
							}  //fin de for 
		 
						}/* fin de lista null*/
					} catch (Exception e) {
					logger.error("Error UtilsCarroCompra.addItemCarro : " + e);
					logger.debug("Error UtilsCarroCompra.addItemCarro : " + e);
				}
				return datos_e;
			}
			
			
			
			/**
			 * 
			 * @param rb
			 * @param ses_loc_id
			 * @param l_torec
			 * @param listaCarro
			 * @param biz
			 * @param top
			 * @param idProductoAgregado
			 * @return
			 */
			public synchronized IValueSet getPromoMiCarro(ResourceBundle rb, List l_torec,
					 List listaCarro, BizDelegate biz, IValueSet top, long total, String ses_loc_id, String ses_colaborador, String ses_cli_rut) {
		 		double descuento_promo = 0;
			 	double descuento_promo_tc = 0;
				String desc_promo = "";
				try {
					// Obtener datos de la promomocion
					doRecalculoCriterio recalculoDTO_tc = new doRecalculoCriterio();
					recalculoDTO_tc.setCuotas(Integer.parseInt(rb.getString("promociones.tjacredito.cuotas")));
					recalculoDTO_tc.setF_pago(rb.getString("promociones.tjacredito.formapago"));
					recalculoDTO_tc.setId_local(Integer.parseInt(ses_loc_id)); //session.getAttribute("ses_loc_id").toString()
					recalculoDTO_tc.setGrupos_tcp(l_torec);
					if (Boolean.valueOf(String.valueOf(ses_colaborador)).booleanValue()) //session.getAttribute("ses_colaborador")
						recalculoDTO_tc.setRutColaborador(new Long(ses_cli_rut)); //session.getAttribute("ses_cli_rut").toString()
					doRecalculoCriterio recalculoDTO = new doRecalculoCriterio();
					recalculoDTO.setCuotas(Integer.parseInt(rb.getString("promociones.jumbomas.cuotas")));
					recalculoDTO.setF_pago(rb.getString("promociones.jumbomas.formapago"));
					recalculoDTO.setId_local(Integer.parseInt(ses_loc_id)); //session.getAttribute("ses_loc_id").toString()
					recalculoDTO.setGrupos_tcp(l_torec);
					if (Boolean.valueOf(String.valueOf(ses_colaborador)).booleanValue())  //session.getAttribute("ses_colaborador")
						recalculoDTO.setRutColaborador(new Long(ses_cli_rut)); //session.getAttribute("ses_cli_rut").toString()
					List l_prod = new ArrayList();
					if(listaCarro != null && listaCarro.size() > 0){
						for (int i = 0; i < listaCarro.size(); i++) {
							MiCarroDTO car = (MiCarroDTO) listaCarro.get(i);
							if (car.tieneStock()) {
								ProductoPromosDTO pro = new ProductoPromosDTO();
								pro.setId_producto(car.getId_bo());
								pro.setCod_barra(car.getCodbarra());
								pro.setSeccion_sap(car.getCatsap());
								pro.setCant_solicitada(car.getCantidad());
								if (car.getPesable() != null
										&& car.getPesable().equals("S"))
									pro.setPesable("P");
								else
									pro.setPesable("C");
								pro.setPrecio_lista(car.getPrecio());
								l_prod.add(pro);
							}
						}
				    }
					recalculoDTO_tc.setProductos(l_prod);
					recalculoDTO.setProductos(l_prod);
					if (l_prod != null && l_prod.size() > 0) {
						// ///////////////////////////////////////////////////
						// CALCULO
						doRecalculoResultado resultado_tc = biz.doRecalculoPromocion(recalculoDTO_tc);
						doRecalculoResultado resultado = biz.doRecalculoPromocion(recalculoDTO);
						// ///////////////////////////////////////////////////
						List l_promo = resultado.getPromociones();
						for (int i = 0; l_promo != null && i < l_promo.size(); i++) {
							PromocionDTO promocion = (PromocionDTO) l_promo.get(i);
							logger.debug("Promocion: " + promocion.getCod_promo() + "-" + promocion.getDescuento1() + "-" + promocion.getDescr());
							desc_promo += promocion.getDescr() + "--" + Formatos.formatoPrecioFO(promocion.getDescuento1()) + "#";
						}
						// Se cambia a descuento de la orden
						descuento_promo = resultado.getDescuento_pedido();
						if (total < resultado.getDescuento_pedido()) {
							descuento_promo = total;
						}
						descuento_promo_tc = resultado_tc.getDescuento_pedido();
						if (total < resultado_tc.getDescuento_pedido())
							descuento_promo_tc = total;
					}
					if(top != null){
						top.setVariable("{total_desc_tc}", Formatos.formatoPrecioFO(total - descuento_promo_tc));
						top.setVariable("{total_desc_tc_sf}", Formatos.formatoPrecioFO(descuento_promo_tc));
						top.setVariable("{total_desc}", Formatos.formatoPrecioFO(total - descuento_promo));
						top.setVariable("{total_desc_sf}", Formatos.formatoPrecioFO(descuento_promo));
						top.setVariable("{promo_desc}", desc_promo);
						top.setVariable("{total}", Formatos.formatoPrecioFO(total) + "");
						top.setVariable("{cant_reg}", listaCarro.size() + "");
						top.setVariable("total_carro", Long.toString(total));
						// top.setVariable("{id_prod_add}", idProductoAgregado +"" );
					}else{
						total = 0;
						top = new ValueSet();
						top.setVariable("{total_desc_tc}", Formatos.formatoPrecioFO(total ));
						top.setVariable("{total_desc_tc_sf}", Formatos.formatoPrecioFO(0));
						top.setVariable("{total_desc}", Formatos.formatoPrecioFO(total ));
						top.setVariable("{total_desc_sf}", Formatos.formatoPrecioFO(0));
						top.setVariable("{promo_desc}", desc_promo);
						top.setVariable("{total}", Formatos.formatoPrecioFO(total) + "");
						top.setVariable("{cant_reg}", listaCarro.size() + "");
						top.setVariable("total_carro", Long.toString(total));
					}
				} catch (SystemException e) {
					total = 0;
					top = new ValueSet();
					top.setVariable("{total_desc_tc}", Formatos.formatoPrecioFO(total ));
					top.setVariable("{total_desc_tc_sf}", Formatos.formatoPrecioFO(0));
					top.setVariable("{total_desc}", Formatos.formatoPrecioFO(total ));
					top.setVariable("{total_desc_sf}", Formatos.formatoPrecioFO(0));
					top.setVariable("{promo_desc}", desc_promo);
					top.setVariable("{total}", Formatos.formatoPrecioFO(total) + "");
					top.setVariable("{cant_reg}", listaCarro.size() + "");
					top.setVariable("total_carro", Long.toString(total));
					logger.error("Error UtilsCarroCompra.en libreria de promociones, no presenta promociones: " + e.getMessage());
					 
				}
				return top;
			}

	private void agregaProductosSinCategoriaActiva(List productosSinCategoria, List listaCat, boolean newProduct, IValueSet top, ResourceBundle rb, List idsProductos, String idProducto, List endListaCarro)
	{
		IValueSet nivel = new ValueSet();
		nivel.setVariable("{cat_id}",  new Long(0));   
		nivel.setVariable("{cat_nombre}","Otros");
		nivel.setVariable("{cat_tipo}", "C");
		nivel.setVariable("{cat_descripcion}", "Otros productos agregados");
		nivel.setVariable("{cat_padre}", "0");
		nivel.setVariable("{link_categoria}",generaCabeceraCategoria((String)nivel.getVariable("{cat_nombre}")));
		
		for (Iterator iterator = productosSinCategoria.iterator(); iterator.hasNext();) 
		{
			MiCarroDTO object = (MiCarroDTO)iterator.next();
					
			endListaCarro.add(object);		
		}
		
		Collections.sort(productosSinCategoria);
    	List listaSet = addItemList(newProduct, productosSinCategoria, top, rb, idsProductos, Long.parseLong(idProducto), contador);
    	contador ++;
    	if( endListaCarro.size() > contador){ 
    		contador = endListaCarro.size() ;
    	}else if(endListaCarro.size() < contador ){
    		contador = endListaCarro.size() - 1;
    	}
    	nivel.setDynamicValueSets("lista_carro", listaSet);
	    listaCat.add(nivel);	
	}

	private String generaLinkCategoria(String cat_id, String numero, String cat_nombre)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<a style='color:#FFF;' href='javascript:muestracabecera(");
		sb.append(cat_id);
		sb.append(",");
		sb.append(numero);
		sb.append(")' id='");
		sb.append(cat_id);
		sb.append("' class='cabecera' >");
		sb.append(cat_nombre);
		sb.append("</a>");
		return sb.toString();
	}
	
	private String generaCabeceraCategoria(String cat_nombre)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='color:#FFF;' class='cabecera' >");
		sb.append(cat_nombre);
		sb.append("</span>");
		return sb.toString();
	}
}

