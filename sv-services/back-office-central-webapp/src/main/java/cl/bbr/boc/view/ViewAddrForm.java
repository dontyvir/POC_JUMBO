package cl.bbr.boc.view;

import java.util.ArrayList;
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
import cl.bbr.jumbocl.clientes.dto.ComunasDTO;
import cl.bbr.jumbocl.clientes.dto.DireccionesDTO;
import cl.bbr.jumbocl.clientes.dto.TipoCalleDTO;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.contenidos.dto.EstadoDTO;
import cl.bbr.jumbocl.pedidos.dto.PoligonoxComunaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * Llena el popup de direcciones
 * @author BBRI 
 */
public class ViewAddrForm extends Command {
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		int id_dir;
		int id_cliente;
		int mje_ok = 0;

		logger.debug("User: " + usr.getLogin());
		// 1. Parámetros de inicialización servlet
		// Recupera pagina desde web.xml
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);
		// 2. Procesa parámetros del request

		
		if (req.getParameter("idcliente") != null){
			id_cliente = Integer.parseInt(req.getParameter("idcliente"));
		}else{
			id_cliente = 0;
		}
		if (req.getParameter("dirid") != null)
			id_dir = Integer.parseInt(req.getParameter("dirid"));
		else{
			id_dir = 0;
		}
		
		if (req.getParameter("mje_ok") != null){
			mje_ok = Integer.parseInt(req.getParameter("mje_ok"));
		}
		
		//3. Template

		View salida = new View(res);
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4. Rutinas Dinámicas

		// 4.0 Bizdelegator
		BizDelegate bizDelegate = new BizDelegate();
		//Recupera Datos de Direccion segun  Id Cliente
		DireccionesDTO direc = bizDelegate.getDireccionByIdDir(id_dir);
		top.setVariable("{zona}", String.valueOf(direc.getId_zon()));
		top.setVariable("{nom_zona}", String.valueOf(direc.getNom_zona()));
		logger.debug("El nombre de región es: " +  direc.getNom_region());
		top.setVariable("{comuna}", String.valueOf(direc.getComuna()));
		top.setVariable("{alias}", String.valueOf(direc.getAlias()));
		top.setVariable("{calle}", String.valueOf(direc.getCalle()));
		top.setVariable("{numero}", String.valueOf(direc.getNumero()));
		top.setVariable("{depto}", String.valueOf(direc.getDepto()));
		top.setVariable("{comentario}", String.valueOf(direc.getComentarios()));
		top.setVariable("{fec_crea}", Formatos.frmFechaHora(direc.getFec_crea()));
		top.setVariable("{com}", String.valueOf(direc.getId_com()));
		if (direc.getDir_conflictiva().equals("1")){
		    top.setVariable("{dir_conflictiva_check}", "checked");
		}else{
		    top.setVariable("{dir_conflictiva_check}", "");
		}
		top.setVariable("{dir_conflictiva}", direc.getDir_conflictiva());
		top.setVariable("{comentario_dir_conflictiva}", direc.getDir_conflictiva_comentario());
		
		// se obtiene el local actual de la direccion
		long comlocal = -1;
		comlocal = direc.getId_loc();
		String nomlocal = "";
		// se obtiene la comuna actual de la direccion
		long comcomuna = -1;
		comcomuna = direc.getId_com();
		// se obtiene la comuna actual de la direccion
		//long dirzona = -1;
		//dirzona = direc.getId_zon();
		// se obtiene el poligono actual de la direccion
		long dirpoligono = -1;
		dirpoligono = direc.getId_poligono();
		
		// se obtiene el tipo de calle de la direccion
		String comtipo = "";
		comtipo = direc.getNom_tipocalle();

		// se obtiene el estado actual de la direccion
		String comestado = "";
		comestado = String.valueOf(direc.getEstado());
		logger.debug("Este es el estado que trae el direc:" + direc.getEstado());
		
		
		//listado de comunas
		ArrayList comunas = new ArrayList();
		//List listComunas = bizDelegate.getComunas();
		List listComunas = bizDelegate.getComunasConPoligonos();

		for (int i = 0; i < listComunas.size(); i++) {
			IValueSet fila_com = new ValueSet();
			ComunasDTO com = (ComunasDTO) listComunas.get(i);
			fila_com.setVariable("{id_comuna}", String.valueOf(com.getId_comuna()));
			fila_com.setVariable("{nom_comuna}", String.valueOf(com.getNombre())+" - " +String.valueOf(com.getReg_nombre()));
			
			if (comcomuna != -1 && comcomuna == com.getId_comuna()) {
				fila_com.setVariable("{sel_com}", "selected");
			} else {
				fila_com.setVariable("{sel_com}", "");
			}
			comunas.add(fila_com);
		}		
		
		
		// Listado de todos los poligonos
		List listaPoligonoXComuna = new ArrayList();
		listaPoligonoXComuna = bizDelegate.getPoligonosXComuna(direc.getId_com());
		List poligonos = new ArrayList();
		for(int i =0; i<listaPoligonoXComuna.size(); i++){
			IValueSet fila_pol = new ValueSet();			
			PoligonoxComunaDTO pxc = (PoligonoxComunaDTO) listaPoligonoXComuna.get(i);
			
			if (pxc.getId_zona() > 0 && pxc.getId_comuna() > 0){
				fila_pol.setVariable("{id_poligono}", String.valueOf(pxc.getId_poligono()));
				fila_pol.setVariable("{num_poligono}", String.valueOf(pxc.getNum_poligono()));
				fila_pol.setVariable("{desc_poligono}", String.valueOf(pxc.getDesc_poligono()));
				fila_pol.setVariable("{id_local}", String.valueOf(pxc.getId_local()));
				fila_pol.setVariable("{nom_local}", String.valueOf(pxc.getNom_local()));
				fila_pol.setVariable("{id_zona}", String.valueOf(pxc.getId_zona()));
				fila_pol.setVariable("{nom_zona}", String.valueOf(pxc.getNom_zona()));
				fila_pol.setVariable("{id_comuna}", String.valueOf(pxc.getId_comuna()));
				fila_pol.setVariable("{nom_comuna}", String.valueOf(pxc.getNom_comuna()));
				if (dirpoligono != -1 && dirpoligono == pxc.getId_poligono()) {
				    fila_pol.setVariable("{sel_pol}", "selected");
				} else {
				    fila_pol.setVariable("{sel_pol}", "");
				}
				poligonos.add(fila_pol);
			}
		}
		
/*      //Listado de Zonas
		ArrayList zonas = new ArrayList();
		List listZonas = bizDelegate.getZonasByComuna(direc.getId_com());

		if ( listZonas.size() > 0 ){
			for (int i = 0; i < listZonas.size(); i++) {
				IValueSet fila_zon = new ValueSet();
				ZonaDTO zon = (ZonaDTO) listZonas.get(i);
				
				ZonaDTO listLoc = bizDelegate.getZonaById(zon.getId_zona());
				fila_zon.setVariable("{id_zona}", String.valueOf(zon.getId_zona()));
				fila_zon.setVariable("{nom_zona}", String.valueOf(zon.getNombre()) + " ("+ listLoc.getNom_local()+")");
				if (dirzona != -1 && dirzona == zon.getId_zona()) {
					fila_zon.setVariable("{sel_zon}", "selected");
				} else {
					fila_zon.setVariable("{sel_zon}", "");
				}
				zonas.add(fila_zon);
		
				logger.debug("Esto es lo que devuelve listLoc " + listLoc.getNom_local() );
			}
		}
*/


		//listado de tipos de calle
		ArrayList tipCalles = new ArrayList();

		List listTipCalles = bizDelegate.getTiposCalle();

		for (int i = 0; i < listTipCalles.size(); i++) {
			IValueSet fila_est = new ValueSet();
			TipoCalleDTO tipCalle = (TipoCalleDTO) listTipCalles.get(i);
			fila_est.setVariable("{id_tcalle}", String.valueOf(tipCalle.getId()));
			fila_est.setVariable("{nom_tcalle}", String.valueOf(tipCalle.getNombre()));
			if (comtipo != "" && comtipo.equals(String.valueOf(tipCalle.getNombre()))) {
				fila_est.setVariable("{sel_tca}", "selected");
			} else {
				fila_est.setVariable("{sel_tca}", "");
			}
			tipCalles.add(fila_est);
		}
		
		//estados de direcciones
		ArrayList estados = new ArrayList();

		List listEstados = bizDelegate.getEstadosByVis("ALL","S");

		for (int i = 0; i < listEstados.size(); i++) {
			IValueSet fila_est = new ValueSet();
			EstadoDTO est1 = (EstadoDTO) listEstados.get(i);
			fila_est.setVariable("{id_estado}", String.valueOf(est1.getId_estado()));
			fila_est.setVariable("{nom_estado}", String.valueOf(est1
					.getNombre()));
			logger.debug("Esto es lo que trae getId_estado:" + String.valueOf(est1.getId_estado()));
			if (comestado != ""
					&& comestado.equals(String.valueOf(est1.getId_estado()))){
				fila_est.setVariable("{sel_estd}", "selected");
				logger.debug(comestado + "es igual " + String.valueOf(est1.getId_estado()));
			} else {
				fila_est.setVariable("{sel_estd}", "");
				logger.debug(comestado + "No es igual " + String.valueOf(est1.getId_estado()));
			}
			estados.add(fila_est);
		}
		
		if (mje_ok == 1)
			top.setVariable("{mensaje}","Dirección modificada satisfactoriamente");
		else if (mje_ok == 2)
			top.setVariable("{mensaje}","No se pudo modificar dirección");
		else
			top.setVariable("{mensaje}","");
		
		top.setVariable("{dir_id}",String.valueOf(id_dir) );
		top.setVariable("{id_cliente}",String.valueOf(id_cliente) );
		top.setVariable("{nom_local}",nomlocal);
		top.setVariable("{id_local}",comlocal+"");
		
		//5 Paginador

        // 6. Setea variables bloques
        //top.setDynamicValueSets("ZONAS", zonas);
        //top.setDynamicValueSets("LOCALES", locales);
		top.setDynamicValueSets("LST_POLIGONOS_COMUNA", poligonos);
		top.setDynamicValueSets("COMUNAS", comunas);
		top.setDynamicValueSets("T_CALLE", tipCalles);
		top.setDynamicValueSets("EST_DIR", estados);

		//		 7. Salida Final
		String result = tem.toString(top);

		salida.setHtmlOut(result);
		salida.Output();
	}
}