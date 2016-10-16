package cl.bbr.boc.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.common.model.CuponEntity;
import cl.bbr.jumbocl.common.model.CuponPorProducto;
import cl.bbr.jumbocl.common.model.RubroEntity;
import cl.bbr.jumbocl.common.model.SeccionEntity;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * formulario para el ingreso de nuevos productos al monitor de productos
 * 
 * @author JoLazoGu
 */
public class ViewCuponAsociaProductosForm extends Command {
	private final static long serialVersionUID = 1;

	protected void Execute(HttpServletRequest req, HttpServletResponse res,
			UserDTO usr) throws Exception {

		View salida = new View(res);

		logger.debug("User: " + usr.getLogin());

		// Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		String UrlResponse = getServletConfig().getInitParameter("UrlResponse");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);

		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// parámetros
		String id_cup_dto = "";
		String dato_tipo_prodcuto = "";
		String dato_tipo_rubro = "";
		String dato_tipo_seccion = "";
		String rad_tipo = "";
		String rad_buscar = "";
		String codDesc = "";
		String rad_despacho = "";
		String id_cup_dto_up = "";
		String rad_Publico = "";

		if (req.getParameter("dato_tipo_prodcuto") != null) {
			dato_tipo_prodcuto = req.getParameter("dato_tipo_prodcuto");
		}
		if (req.getParameter("dato_tipo_rubro") != null) {
			dato_tipo_rubro = req.getParameter("dato_tipo_rubro");
		}
		if (req.getParameter("dato_tipo_seccion") != null) {
			dato_tipo_seccion = req.getParameter("dato_tipo_seccion");
		}
		if (req.getParameter("rad_tipo") != null) {
			rad_tipo = req.getParameter("rad_tipo");
		}
		if (req.getParameter("rad_buscar") != null) {
			rad_buscar = req.getParameter("rad_buscar");
		}
		if (req.getParameter("codDesc") != null) {
			codDesc = req.getParameter("codDesc").toLowerCase();
		}
		if (req.getParameter("id_cup_dto") != null) {
			id_cup_dto = req.getParameter("id_cup_dto");
		}
		if (req.getParameter("rad_despacho") != null) {
			rad_despacho = req.getParameter("rad_despacho");
		}
		if (req.getParameter("id_cup_dto_up") != null) {
			id_cup_dto_up = req.getParameter("id_cup_dto_up");
		}
		if (req.getParameter("rad_Publico") != null) {
			rad_Publico = req.getParameter("rad_Publico");
		}
		

		BizDelegate biz = new BizDelegate();
		CuponEntity cupon = new CuponEntity();
		
		if(!id_cup_dto.equals(""))
			cupon.setId_cup_dto(Long.parseLong(id_cup_dto));
		
		if(!id_cup_dto_up.equals(""))
			cupon.setId_cup_dto(Long.parseLong(id_cup_dto_up));
			
		cupon = biz.getDatoCuponPorId(cupon);
		
		if (id_cup_dto != null && rad_tipo.equals("")) {
			
			top.setDynamicValueSets("RUBROS", listaRubros());
			top.setDynamicValueSets("SECCIONES", listaSecciones());
			top.setVariable("{cod_cupon}", cupon.getCodigo());
			top.setVariable("{id_cup_dto}", id_cup_dto);
			top.setVariable("{mensaje}", "");
			
			if(cupon.getTipo().equals("R")){
				
				top.setVariable("{check_2}", "checked");
				top.setVariable("{check_6}", "disabled");
				top.setVariable("{check_7}", "disabled");
				top.setVariable("{buscador}", "disabled");
				top.setVariable("{listRubros}", "block");
				top.setVariable("{listSecciones}", "none");
				top.setVariable("{listProductos}", "none");
				top.setVariable("{btnAsociar}", "none");
			}
			else if(cupon.getTipo().equals("S")) {
				
				top.setVariable("{check_3}", "checked");
				top.setVariable("{check_6}", "disabled");
				top.setVariable("{check_7}", "disabled");
				top.setVariable("{buscador}", "disabled");
				top.setVariable("{listRubros}", "none");
				top.setVariable("{listSecciones}", "block");
				top.setVariable("{listProductos}", "none");
				top.setVariable("{btnAsociar}", "none");
			}
			else if(cupon.getTipo().equals("P")) {
				
				top.setVariable("{check_1}", "checked");
				top.setVariable("{check_6}", "checked");
				//top.setVariable("{check_9}", "checked");
				top.setVariable("{buscador}", "");
				top.setVariable("{listRubros}", "none");
				top.setVariable("{listSecciones}", "none");
				top.setVariable("{listProductos}", "block");
				top.setVariable("{btnAsociar}", "none");
			}
			else if(cupon.getTipo().equals("TS")) {
				
				top.setVariable("{check_4}", "checked");
				top.setVariable("{check_6}", "disabled");
				top.setVariable("{check_7}", "disabled");
				top.setVariable("{buscador}", "disabled");
				top.setVariable("{listRubros}", "none");
				top.setVariable("{listSecciones}", "none");
				top.setVariable("{listProductos}", "none");
				top.setVariable("{btnAsociar}", "block");
			}
			else if(cupon.getTipo().equals("D")) {
				
				top.setVariable("{check_5}", "checked");
				top.setVariable("{check_6}", "disabled");
				top.setVariable("{check_7}", "disabled");
				top.setVariable("{buscador}", "disabled");
				top.setVariable("{listRubros}", "none");
				top.setVariable("{listSecciones}", "none");
				top.setVariable("{listProductos}", "none");
				top.setVariable("{btnAsociar}", "block");
			}
			else{
				
				top.setVariable("{check_1}", "checked");
				top.setVariable("{check_6}", "checked");
				top.setVariable("{check_9}", "checked");
				top.setVariable("{buscador}", "");
				top.setVariable("{listRubros}", "none");
				top.setVariable("{listSecciones}", "none");
				top.setVariable("{listProductos}", "block");
				top.setVariable("{btnAsociar}", "none");
				
			}
			
			if(cupon.getDespacho() == 1)
				top.setVariable("{check_8}", "checked");
			else
				top.setVariable("{check_9}", "checked");
				
			top.setVariable("{codDesc}", "");
			top.setVariable("{listaAsociaciones}", "");
			top.setVariable("{rad_Publico}", rad_Publico);
			top.setDynamicValueSets("LISTA_ASOCIACIONES", listaAsociaciones(id_cup_dto));

			CuponEntity ce = new CuponEntity();
			ce.setId_cup_dto(Long.parseLong(id_cup_dto));
			ce = biz.getDatoCuponPorId(ce);

			if (listaAsociaciones(id_cup_dto).size() > 0 || ce.getTipo().equals("D")) {
				top.setVariable("{check_10}", "block");
				top.setVariable("{id_cup_dto_up}", id_cup_dto);
			} else {
				top.setVariable("{check_10}", "none");
			}

			salidaHtml(top, usr, tem, salida);

		} else if (!rad_buscar.equals("") && dato_tipo_prodcuto.equals("") && id_cup_dto_up.equals("")) {

			if (rad_buscar.equals("cod")) {
			
				top.setVariable("{codDesc}", codDesc);
				top.setDynamicValueSets("PRODUCTOS", listaProductosPorCod(codDesc));
			
			}else if (rad_buscar.equals("desc")) {

				top.setVariable("{codDesc}", codDesc);
				top.setDynamicValueSets("PRODUCTOS", listaProductosPorDesc(codDesc));
			}

			top.setDynamicValueSets("RUBROS", listaRubros());
			top.setDynamicValueSets("SECCIONES", listaSecciones());
			top.setVariable("{cod_cupon}", cupon.getCodigo());
			top.setVariable("{id_cup_dto}", id_cup_dto);
			top.setVariable("{mensaje}", "");
			top.setVariable("{check_1}", "checked");
			top.setVariable("{check_6}", "checked");
			top.setVariable("{check_9}", "checked");
			
			top.setVariable("{listRubros}", "none");
			top.setVariable("{listSecciones}", "none");
			top.setVariable("{listProductos}", "block");
			top.setVariable("{btnAsociar}", "none");
			
			top.setVariable("{listaAsociaciones}", "");
			top.setVariable("{rad_Publico}", rad_Publico);
			top.setDynamicValueSets("LISTA_ASOCIACIONES", listaAsociaciones(id_cup_dto));

			CuponEntity ce = new CuponEntity();
			ce.setId_cup_dto(Long.parseLong(id_cup_dto));
			ce = biz.getDatoCuponPorId(ce);

			if (listaAsociaciones(id_cup_dto).size() > 0 || ce.getTipo().equals("D")) {
				top.setVariable("{check_10}", "block");
				top.setVariable("{id_cup_dto_up}", id_cup_dto);
			} else {
				top.setVariable("{check_10}", "none");
			}

			salidaHtml(top, usr, tem, salida);

		} else if (id_cup_dto != null && !rad_tipo.equals("") && id_cup_dto_up.equals("")) {

			CuponPorProducto cpp = new CuponPorProducto();
			CuponEntity ce = new CuponEntity();

			if (rad_tipo.equals("S")) {

				cpp.setId_cup_dto(Long.parseLong(id_cup_dto));
				cpp.setId_seccion(Integer.parseInt(dato_tipo_seccion));
				biz.setCuponAsociarTipo(cpp, rad_tipo, usr.getId_usuario(), rad_despacho);
				top.setVariable("{check_3}", "checked");
				top.setVariable("{check_6}", "disabled");
				top.setVariable("{check_7}", "disabled");
				top.setVariable("{buscador}", "disabled");
				top.setVariable("{listRubros}", "none");
				top.setVariable("{listSecciones}", "block");
				top.setVariable("{listProductos}", "none");
				top.setVariable("{btnAsociar}", "none");
			
			} else if (rad_tipo.equals("R")) {

				String[] seccionRubro = dato_tipo_rubro.split("/");
				cpp.setId_rubro(Integer.parseInt(seccionRubro[0]));
				cpp.setId_seccion(Integer.parseInt(seccionRubro[1]));
				cpp.setId_cup_dto(Long.parseLong(id_cup_dto));
				biz.setCuponAsociarTipo(cpp, rad_tipo, usr.getId_usuario(), rad_despacho);
				top.setVariable("{check_2}", "checked");
				top.setVariable("{check_6}", "disabled");
				top.setVariable("{check_7}", "disabled");
				top.setVariable("{buscador}", "disabled");
				top.setVariable("{listRubros}", "block");
				top.setVariable("{listSecciones}", "none");
				top.setVariable("{listProductos}", "none");
				top.setVariable("{btnAsociar}", "none");
				

			} else if (rad_tipo.equals("P")) {

				cpp.setId_cup_dto(Long.parseLong(id_cup_dto));
				cpp.setId_producto(Integer.parseInt(dato_tipo_prodcuto));
				biz.setCuponAsociarTipo(cpp, rad_tipo, usr.getId_usuario(), rad_despacho);
				top.setVariable("{check_1}", "checked");
				top.setVariable("{check_6}", "checked");
				top.setVariable("{check_9}", "checked");
				top.setVariable("{buscador}", "");
				top.setVariable("{listRubros}", "none");
				top.setVariable("{listSecciones}", "none");
				top.setVariable("{listProductos}", "block");
				top.setVariable("{btnAsociar}", "none");

			} else if (rad_tipo.equals("TS")) {

				ce.setId_cup_dto(Long.parseLong(id_cup_dto));
				cpp.setId_cup_dto(Long.parseLong(id_cup_dto));
				biz.setCuponAsociarTipo(cpp, rad_tipo, usr.getId_usuario(), rad_despacho);
				biz.setTodasLasSeccionesAsociado(ce);
				top.setVariable("{check_4}", "checked");
				top.setVariable("{check_6}", "disabled");
				top.setVariable("{check_7}", "disabled");
				top.setVariable("{buscador}", "disabled");
				top.setVariable("{listRubros}", "none");
				top.setVariable("{listSecciones}", "none");
				top.setVariable("{listProductos}", "none");
				top.setVariable("{btnAsociar}", "block");

			} else if (rad_tipo.equals("D")) {

				cpp.setId_cup_dto(Long.parseLong(id_cup_dto));
				biz.setCuponAsociarTipo(cpp, rad_tipo, usr.getId_usuario(), String.valueOf("1"));
				top.setVariable("{check_5}", "checked");
				top.setVariable("{check_6}", "disabled");
				top.setVariable("{check_7}", "disabled");
				top.setVariable("{buscador}", "disabled");
				top.setVariable("{listRubros}", "none");
				top.setVariable("{listSecciones}", "none");
				top.setVariable("{listProductos}", "none");
				top.setVariable("{btnAsociar}", "block");
			}

			top.setDynamicValueSets("RUBROS", listaRubros());
			top.setDynamicValueSets("SECCIONES", listaSecciones());
			top.setVariable("{id_cup_dto}", id_cup_dto);
			top.setVariable("{cod_cupon}", cupon.getCodigo());
			top.setVariable("{mensaje}", "");
			top.setVariable("{codDesc}", "");
			top.setVariable("{listaAsociaciones}", "");
			top.setVariable("{rad_Publico}", rad_Publico);
			top.setDynamicValueSets("LISTA_ASOCIACIONES", listaAsociaciones(cpp,id_cup_dto));

			ce.setId_cup_dto(Long.parseLong(id_cup_dto));
			ce = biz.getDatoCuponPorId(ce);
			
			if(ce.getDespacho() == 1)
				top.setVariable("{check_8}", "checked");
			else
				top.setVariable("{check_9}", "checked");
			
		
			if (listaAsociaciones(cpp,id_cup_dto).size() > 0 || ce.getTipo().equals("D")) {
				top.setVariable("{check_10}", "block");
				top.setVariable("{id_cup_dto_up}", id_cup_dto);
			} else {
				top.setVariable("{check_10}", "none");
			}

			salidaHtml(top, usr, tem, salida);

		} else if (!id_cup_dto_up.equals("")) {

			CuponEntity ce = new CuponEntity();
			ce.setId_cup_dto(Long.parseLong(id_cup_dto_up));
			ce = biz.getDatoCuponPorId(ce);
			ce.setDespacho(Integer.parseInt(rad_despacho));
			ce.setPublico(Integer.parseInt(rad_Publico));
			biz.setGuardarCupon(ce);

			top.setVariable("{hdr_nombre}",usr.getNombre() + " " + usr.getApe_paterno());
			Date now = new Date();
			top.setVariable("{hdr_fecha}", now.toString());

			res.sendRedirect(UrlResponse + "?id_cup_dto=" + ce.getId_cup_dto());

		}
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public List listaRubros() throws Exception {

		BizDelegate biz = new BizDelegate();
		ArrayList rubros = new ArrayList();
		List listaRubros = biz.getListaRubros();

		for (int i = 0; i < listaRubros.size(); i++) {
			IValueSet fila_tip = new ValueSet();
			RubroEntity rubro = (RubroEntity) listaRubros.get(i);
			
			fila_tip.setVariable("{dato_tipo_cod}",	String.valueOf(rubro.getIdSeccion()) + "/" + String.valueOf(rubro.getIdRubro()));
			fila_tip.setVariable("{dato_tipo_desc}", rubro.getNombreRubro().toUpperCase());
			
			rubros.add(fila_tip);
		}

		return rubros;
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public List listaSecciones() throws Exception {

		BizDelegate biz = new BizDelegate();
		ArrayList secciones = new ArrayList();
		List listaSecciones = biz.getListaSecciones();

		for (int i = 0; i < listaSecciones.size(); i++) {
			IValueSet fila_tip = new ValueSet();
			SeccionEntity seccion = (SeccionEntity) listaSecciones.get(i);
			
			fila_tip.setVariable("{dato_tipo_cod}", String.valueOf(seccion.getIdSeccion()));
			fila_tip.setVariable("{dato_tipo_desc}", seccion.getNombreSeccion().toUpperCase());
			
			secciones.add(fila_tip);
		}

		return secciones;
	}
	
	/**
	 * 
	 * @param cpp
	 * @param id_cup_dto
	 * @return
	 * @throws Exception
	 */
	public List listaAsociaciones(CuponPorProducto cpp, String id_cup_dto) throws Exception {

		BizDelegate biz = new BizDelegate();
		ArrayList asociaciones = new ArrayList();
		List listCuponAsociado = biz.getListaCuponAsociado(Integer.parseInt(id_cup_dto));

		for (int i = 0; i < listCuponAsociado.size(); i++) {
			IValueSet fila_tip = new ValueSet();
			cpp = (CuponPorProducto) listCuponAsociado.get(i);
			
			fila_tip.setVariable("{listaIdProdRubSecc}", String.valueOf(cpp.getId_prodRubSecc()));
			fila_tip.setVariable("{listaNombreProdRubSecc}", String.valueOf(cpp.getNombreProdRubSecc()).toUpperCase());
			
			asociaciones.add(fila_tip);
		}

		return asociaciones;
	}
	
	/**
	 * 
	 * @param id_cup_dto
	 * @return
	 * @throws Exception
	 */
	public List listaAsociaciones(String id_cup_dto) throws Exception {

		BizDelegate biz = new BizDelegate();

		ArrayList asociaciones = new ArrayList();
		List listCuponAsociado = biz.getListaCuponAsociado(Integer.parseInt(id_cup_dto));

		for (int i = 0; i < listCuponAsociado.size(); i++) {
			IValueSet fila_tip = new ValueSet();
			CuponPorProducto cpp = (CuponPorProducto) listCuponAsociado.get(i);
			
			fila_tip.setVariable("{listaIdProdRubSecc}", String.valueOf(cpp.getId_prodRubSecc()));
			fila_tip.setVariable("{listaNombreProdRubSecc}", String.valueOf(cpp.getNombreProdRubSecc().toUpperCase()));
			
			asociaciones.add(fila_tip);
		}

		return asociaciones;
	}
	
	/**
	 * 
	 * @param codDesc
	 * @return
	 * @throws Exception
	 */
	public List listaProductosPorDesc(String codDesc) throws Exception {

		BizDelegate biz = new BizDelegate();
		ArrayList productos = new ArrayList();

		List listProductoEncontrado = biz.getTiposProductosPorDesc(codDesc);

		for (int i = 0; i < listProductoEncontrado.size(); i++) {
			IValueSet fila_tip = new ValueSet();
			CuponEntity ce = (CuponEntity) listProductoEncontrado.get(i);
			
			fila_tip.setVariable("{dato_tipo_cod}", String.valueOf(ce.getDato_tipo_cod()));
			fila_tip.setVariable("{dato_tipo_desc}", String.valueOf(ce.getDato_tipo_desc().toUpperCase()));
			
			productos.add(fila_tip);
		}
		
		return productos;
	}
	
	/**
	 * 
	 * @param codDesc
	 * @return
	 * @throws Exception
	 */
	public List listaProductosPorCod(String codDesc) throws Exception {
		
		BizDelegate biz = new BizDelegate();
		
		ArrayList productos = new ArrayList();
		List listProductoEncontrado = biz.getTiposProductosPorCod(codDesc);

		for (int i = 0; i < listProductoEncontrado.size(); i++) {
			IValueSet fila_tip = new ValueSet();
			CuponEntity ce = (CuponEntity) listProductoEncontrado.get(i);
			
			fila_tip.setVariable("{dato_tipo_cod}", String.valueOf(ce.getDato_tipo_cod()));
			fila_tip.setVariable("{dato_tipo_desc}", String.valueOf(ce.getDato_tipo_desc().toUpperCase()));
			
			productos.add(fila_tip);
		}
		
		return productos;
	}
	
	/**
	 * 
	 * @param top
	 * @param usr
	 * @param tem
	 * @param salida
	 * @throws Exception
	 */
	public void salidaHtml(IValueSet top, UserDTO usr, ITemplate tem, View salida ) throws Exception{
		
		top.setVariable("{hdr_nombre}",usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}", now.toString());

		String result = tem.toString(top);

		salida.setHtmlOut(result);
		salida.Output();
	}

}
