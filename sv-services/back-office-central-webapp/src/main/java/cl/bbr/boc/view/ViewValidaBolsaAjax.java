package cl.bbr.boc.view;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * con un ajax permite cambiar datos de la comuna y la zona sin cargar la pagina 
 * @author BBRI
 */
public class ViewValidaBolsaAjax extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		String cod_sap = new String();
		String cod_bolsa = new String();
		
		BizDelegate bizDelegate = new BizDelegate();
		
		req.setCharacterEncoding("UTF-8");
		res.setContentType("text/html; charset=iso-8859-1");
		
		PrintWriter salida = res.getWriter();
		
		if (req.getParameter("cod_sap") != null){
			cod_sap = req.getParameter("cod_sap");
        }else{
			cod_sap = "";
		}
		
		if (req.getParameter("cod_bolsa") != null){
			cod_bolsa = req.getParameter("cod_bolsa");
        }else{
			cod_bolsa = "";
		}
		
		String result = new String();
		
        if(bizDelegate.validaCodSap(cod_sap)){
			result = "";
		}else{
			result = "Para poder crear la bolsa, primero debe crearse en SAP";
		}
        
        result += "|";
        
        boolean flag = bizDelegate.validaCodBolsa(cod_bolsa); 
		
        if(flag){
			result += "El código de bolsa que intenta ingresar ya existe.";
		}
        
        boolean flag2 = bizDelegate.validaCodBolsaSap(cod_bolsa, cod_sap);
        
        if(flag2){
			result += "El código sap ya esta siendo usado por otra bolsa.";
		}        

        
        System.out.println("---------RESULTADO AJAX="+result);
        
		salida.print(result);
	}
}
