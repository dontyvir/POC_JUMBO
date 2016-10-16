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
import cl.bbr.jumbocl.clientes.dto.CarroCompraDTO;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.promo.lib.dto.ProductoPromosDTO;
import cl.bbr.promo.lib.dto.PromocionDTO;
import cl.bbr.promo.lib.dto.doRecalculoCriterio;
import cl.bbr.promo.lib.dto.doRecalculoResultado;

/**
 * Página que entrega los productos que tiene el cliente en el carro
 * @author carriagada-IT4B
 * 
 */
public class ActualizaTotalesMiCarro extends Command {
    
    protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws CommandException {
		try {
		    // Carga properties
			ResourceBundle rb = ResourceBundle.getBundle("fo");			
			
			// Recupera la sesión del usuario
			HttpSession session = arg0.getSession();
            
            String invitado_id = "";
            long cli_id = 0L;
            cli_id = Long.parseLong((String)session.getAttribute("ses_cli_id"));
            if (session.getAttribute("ses_invitado_id") != null &&
                    !session.getAttribute("ses_invitado_id").toString().equals("")){
                invitado_id = session.getAttribute("ses_invitado_id").toString();
            }

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

			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
            
            List listaCarro = biz.carroComprasGetProductos(cli_id, session.getAttribute("ses_loc_id").toString(), invitado_id);
            
            long total = 0;
            CarroCompraDTO car = null;
            long precio_total = 0;
            for (int i = 0; i < listaCarro.size(); i++) {
                 car = (CarroCompraDTO) listaCarro.get(i); 
                 precio_total = Utils.redondearFO(car.getCantidad() * car.getPrecio());
                 total += precio_total;
            }
                
			// Recuperar los cupones y los tcp de la sesión
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
			
			double descuento_promo = 0;
			String desc_promo = "";
            double descuento_promo_tmas = 0;
            double descuento_promo_tban = 0;
            String desc_promo_tmas = "";
            String desc_promo_tban = "";
            
//          Obtener datos de la promomocion TMAS
            try {
                doRecalculoCriterio recalculoDTO = new doRecalculoCriterio();
                recalculoDTO.setCuotas( Integer.parseInt(rb.getString("promociones.jumbomas.cuotas")) );
                recalculoDTO.setF_pago( rb.getString("promociones.jumbomas.formapago") );
                recalculoDTO.setId_local( Integer.parseInt(session.getAttribute("ses_loc_id").toString()) );
                recalculoDTO.setGrupos_tcp(l_torec);
				//[20121108avc
                if(Boolean.valueOf(String.valueOf(session.getAttribute("ses_colaborador"))).booleanValue())
                    recalculoDTO.setRutColaborador(new Long(session.getAttribute("ses_cli_rut").toString()));
				//]20121108avc


                List l_prod = new ArrayList();
                
                for (int i = 0; i < listaCarro.size(); i++) {
                    car = (CarroCompraDTO) listaCarro.get(i);
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
                    car = (CarroCompraDTO) listaCarro.get(i);
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
            
            session.setAttribute("##_total_descuento_promo_tmas",String.valueOf(total-descuento_promo_tmas));//Solo para el calculo del despacho
            top.setVariable("{total_desc_tmas}", Formatos.formatoPrecioFO(total-descuento_promo_tmas));
            top.setVariable("{total_desc_sf_tmas}", Formatos.formatoPrecioFO(descuento_promo_tmas));
            top.setVariable("{total_desc_tban}", Formatos.formatoPrecioFO(total-descuento_promo_tban));
            top.setVariable("{total_desc_sf_tban}", Formatos.formatoPrecioFO(descuento_promo_tban));
            
			top.setVariable("{total_desc}", Formatos.formatoPrecioFO(total-descuento_promo));
            top.setVariable("{total_desc_sf}", Formatos.formatoPrecioFO(descuento_promo));
			top.setVariable("{promo_desc}", desc_promo );
			top.setVariable("{total}", Formatos.formatoPrecioFO(total) +"" );
			
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

}