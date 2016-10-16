package cl.bbr.bol.command;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.pedidos.dto.FaltanteDTO;
import cl.bbr.jumbocl.pedidos.dto.LogPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductosPedidoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 *   Clase que se activa solamente si el pedido esta en Revision Faltante
 * 	 hay 2 botones , Pasar a En bodega que su valor de boton en B y
 * 	 Repickear , su valor es R
 * 		
 * 
 * @author RMI -DNT
 */

public class SeleccionRevisionFaltante extends Command {

    public void init(ServletConfig config) throws ServletException {
        super.init(config);        
    }

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
    	logger.debug("-----Nombre de la clase Actual-------- " + getClass());
    	View salida = new View(res);
    	String paramUrl = getServletConfig().getInitParameter("TplFile");
        paramUrl = path_html + paramUrl;
        TemplateLoader load = new TemplateLoader(paramUrl);
        ITemplate tem = load.getTemplate();
        BizDelegate bizDelegate = new BizDelegate();
        List listaFaltantes = new ArrayList();
        long id_pedido  = Long.parseLong(req.getParameter("id_pedido"));
        listaFaltantes = bizDelegate.getFaltantesByPedidoId(id_pedido);
		ArrayList faltantes = new ArrayList();
		logger.debug("numero de faltantes:" + listaFaltantes.size());
		for (int i=0; i<listaFaltantes.size(); i++){			
			IValueSet fila = new ValueSet();
			FaltanteDTO faltante1 = new FaltanteDTO();
			faltante1 = (FaltanteDTO)listaFaltantes.get(i);
			fila.setVariable("{cod_prod}"		,String.valueOf(faltante1.getCod_producto()));
			fila.setVariable("{descripcion}"	,faltante1.getDescripcion());
			fila.setVariable("{cant}"			,String.valueOf(Formatos.formatoNum3Dec(faltante1.getCant_faltante())));							
			faltantes.add(fila);
		}	
		IValueSet top = new ValueSet();
		top.setDynamicValueSets("select_faltantes", faltantes);
		
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();	
    }
    }
    

