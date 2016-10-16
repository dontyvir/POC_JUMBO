package cl.bbr.boc.view;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.pedidos.dto.PilaProductoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * Carga el html con las pilas nutricionales de un producto
 * @author imoyano
 */
public class ViewPilasNutricionalesProducto extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
        long idProducto = 0;
		
		BizDelegate bizDelegate = new BizDelegate();
		
		req.setCharacterEncoding("UTF-8");
		res.setContentType("text/html; charset=iso-8859-1");
		
		PrintWriter salida = res.getWriter();
		
		if ( req.getParameter("id_producto") != null ) {
            idProducto= Integer.parseInt(req.getParameter("id_producto"));
        }
        
        String result = "<table width=\"650\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"> " +
                          "<tr> " +
                            "<td> ";
		
        List lPilasDelProducto = bizDelegate.getPilasNutricionalesByProductoFO(idProducto);
        for (int i = 0; i < lPilasDelProducto.size(); i++) {
            
            result += "<div class=\"pila\"> " +
                        "<table width=\"63\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"> " +
                          "<tr> " +
                            "<td><img src=\"img/pila/arriba.gif\" width=\"63\" height=\"9\" /></td> " +
                          "</tr> " +
                          "<tr> " +
                            "<td height=\"25\" valign=\"top\" background=\"img/pila/fondo.gif\"> ";

           PilaProductoDTO pilaProducto = (PilaProductoDTO) lPilasDelProducto.get(i);
           
           result += "<div align=\"center\" class=\"pilatx\">"+pilaProducto.getPila().getNutriente()+"</div> ";
           
           result +=        "</td> " +
                          "</tr> " +
                          "<tr> " +
                            "<td valign=\"middle\" background=\"img/pila/fondo.gif\" class=\"pilatx2\"> ";
           
           Double sNutri = new Double(pilaProducto.getNutrientePorPorcion());
           if ( sNutri.intValue() == pilaProducto.getNutrientePorPorcion() ) {
               result += "<div align=\"center\">"+sNutri.intValue()+" "+pilaProducto.getPila().getUnidad().getUnidad()+"</div>";  
           } else {
               result += "<div align=\"center\">"+pilaProducto.getNutrientePorPorcion()+" "+pilaProducto.getPila().getUnidad().getUnidad()+"</div>";
           }
           
           result +=        "</td> " +
                          "</tr> " +
                          "<tr> " +
                            "<td height=\"22\" valign=\"middle\" background=\"img/pila/porcentage.gif\" class=\"porcentage_pila\">";
           
           Double sPorcen = new Double(pilaProducto.getPorcentaje());
           if ( sPorcen.intValue() == pilaProducto.getPorcentaje() ) {
               result += "<div align=\"center\">"+sPorcen.intValue()+"%</div>";  
           } else {
               result += "<div align=\"center\">"+pilaProducto.getPorcentaje()+"%</div>";
           }           
           
           result +=        "</td> " +
                          "</tr> " +
                          "<tr> " +
                            "<td><img src=\"img/pila/abajo.gif\" width=\"63\" height=\"9\" /></td> " +
                          "</tr> " +
                        "</table> " +
                      "</div>";
           
           result += "<div class=\"pila_del\"><a href=\"javascript:borrarPila("+idProducto+","+pilaProducto.getPila().getIdPila()+")\">[x]</a></div>";
           
        }
        
        if ( lPilasDelProducto.size() == 0 ) {
            result += "Producto no tiene pilas nutricionales";
        }
        
        result +=       "</td>" +
                      "</tr>" +
                    "</table> ";
       
		salida.print(result);
	}
}
