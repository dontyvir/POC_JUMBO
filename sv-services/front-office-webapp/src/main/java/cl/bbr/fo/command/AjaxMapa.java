package cl.bbr.fo.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.fo.bizdelegate.BizDelegate;


/**
 *  Comando que agrega un registro al log del pedido
 *  @author mleiva
 */
public class AjaxMapa extends Command {

    protected void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		//String VerificaNumPoligono = (req.getParameter("VerificaNumPoligono")==null?"NO":req.getParameter("VerificaNumPoligono"));
		//int id_comuna = Integer.parseInt(req.getParameter("id_comuna")==null?"0":req.getParameter("id_comuna"));
		double lat = 0D;
		double lng = 0D;
		long dir_id = 0L;
		
		if (req.getParameter("lat") != null && !req.getParameter("lat").equalsIgnoreCase("")){
		    lat = Double.parseDouble(req.getParameter("lat"));
		}
		if (req.getParameter("lng") != null && !req.getParameter("lng").equalsIgnoreCase("")){
		    lng = Double.parseDouble(req.getParameter("lng"));
		}
		/* direccion[0]= dir.getId()
		 * direccion[1]= dir.getAlias()
		 * direccion[2]= dir.getLoc_cod()
		 * direccion[3]= dir.getZona_id()
		 * direccion[4]= dir.isConfirmada()
		 * direccion[5]= dir.getLatitud()
		 * direccion[6]= dir.getLongitud()
		 * */
		if (req.getParameter("dir_id") != null && !req.getParameter("dir_id").equalsIgnoreCase("")){
		    String direcion[] = req.getParameter("dir_id").split("--");
		    dir_id = Long.parseLong(direcion[0]);
		}
		
		
		// 3. Procesamiento Principal
		BizDelegate biz = new BizDelegate();
		
		/******************************************************************/
		logger.debug("Comienzo Confirmación Dirección en Mapa");
        String respuestaAjax = "";

        try {
            if ((lat != 0D) && (lng != 0D)){
                if(biz.AlmacenaConfirmacionMapa(lat, lng, dir_id)){
                    respuestaAjax = "OK";
                }else{
                    respuestaAjax = "Error, no se pudo confirmar su dirección, intentelo en otro momento.";
                }
            }else{
                respuestaAjax = "Debe seleccionar su dirección dentro del mapa.";
            }
        }catch(Exception e) {
            respuestaAjax = "Ocurrió un error";
            e.printStackTrace();
		}

        res.setContentType("text/xml");
        res.setHeader("Cache-Control", "no-cache");
        res.setCharacterEncoding("UTF-8");

        res.getWriter().write("<xml_respuesta>");
        res.getWriter().write("<mensaje>" + respuestaAjax + "</mensaje>");
        res.getWriter().write("</xml_respuesta>");

        logger.debug("Fin AlmacenaConfirmacionMapa [AJAX]");
	}
}
