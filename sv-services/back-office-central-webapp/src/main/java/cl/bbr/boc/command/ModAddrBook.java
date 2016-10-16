package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.clientes.dto.ProcModAddrBookDTO;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * ModAddrBook Comando Process
 * modifica la libreta de direcciones
 * @author BBRI
 */

public class ModAddrBook extends Command {

 
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

     
     // 1. seteo de Variables del método
     String paramUrl = "";
     long paramDir_id = 0L;
     long paramCli_id = 0L;
     long paramLoc_id = 0L;
     long paramCom_id = 0L;
     long paramZon_id = 0L;
     long paramTip_cal = 0L;
     String paramAlias = "";
     String paramCalle = "";
     String paramNumero = "";
     String paramDepto = "";
     String paramComentario = "";
     String paramDirConflictiva = "";
     String paramDirConflictivaComentario = "";
     String paramPoligono = "";
 //    String paramEstado = "";

     // 2. Procesa parámetros del request

     logger.debug("Procesando parámetros...");

     // 2.1 revision de parametros obligatorios
     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
     if ( req.getParameter("dir_id") == null ){throw new ParametroObligatorioException("dir_id es nulo");}
   //  if ( req.getParameter("local") == null ){throw new ParametroObligatorioException("id_local es nulo");}

     // 2.2 obtiene parametros desde el request
     paramUrl = req.getParameter("url");
     paramDir_id = Long.parseLong(req.getParameter("dir_id")); //long:obligatorio:si
     paramCom_id= Integer.parseInt(req.getParameter("comuna"));
     //paramZon_id= Integer.parseInt(req.getParameter("zona"));
     paramTip_cal = Integer.parseInt(req.getParameter("tipo_calle"));
     paramAlias = req.getParameter("alias");
     paramCalle = req.getParameter("calle");
     paramNumero = req.getParameter("numero");
     paramDepto = req.getParameter("depto");
     paramComentario = req.getParameter("comentario");
     paramCli_id = Long.parseLong(req.getParameter("id_cliente"));
     paramDirConflictiva = (req.getParameter("dir_conflictiva")==null?"0":req.getParameter("dir_conflictiva"));
     paramDirConflictivaComentario = req.getParameter("comentario_dir_conflictiva");
     paramPoligono = req.getParameter("poligono");
     
     int posSeparador = paramPoligono.indexOf("~");
     int Id_Poligono = Integer.parseInt(paramPoligono.substring(0, posSeparador));
     
     //paramEstado = req.getParameter("estado");
     
     
     // 2.3 log de parametros y valores
     logger.debug("url: " + paramUrl);
     logger.debug("dir_id: " + paramDir_id);
     logger.debug("comuna: " + paramCom_id);
     logger.debug("zona: " + paramZon_id);
     logger.debug("tipo_calle: " + paramTip_cal);
     logger.debug("alias: " + paramAlias);
     logger.debug("calle: " + paramCalle);
     logger.debug("numero: " + paramNumero);
     logger.debug("depto: " + paramDepto);
     logger.debug("comentario: " + paramComentario);
     logger.debug("Id cliente: " + paramCli_id);
     logger.debug("dir conflictiva: " + paramDirConflictiva);
     logger.debug("poligono: " + paramPoligono);
    // logger.debug("estado: " + paramEstado);
     /*
      * 3. Procesamiento Principal
      */
    //Aquí se cae
     BizDelegate bizDelegate = new BizDelegate();
     //List loc = bizDelegate.getLocalesByZona(paramZon_id);
     
     //int id_local = bizDelegate.getLocalByPoligono(Id_Poligono);
     
     /*logger.debug("tamaño loc: "+ loc.size() );
     if ( loc.size() > 0 ){
		for (int i = 0; i < loc.size(); i++) {
			logger.debug("entra " + i +" veces");
			LocalDTO ldto = (LocalDTO)loc.get(i);
		    paramLoc_id = ldto.getId_local();  
		   	//Long.parseLong(String.valueOf(loc.get(i)));
		    logger.debug("Id de Local"+paramLoc_id);
		}
	}*/
     
     
     
     ProcModAddrBookDTO modDir = new ProcModAddrBookDTO(paramDir_id, paramCom_id, Id_Poligono, 
    		 paramTip_cal, paramAlias, paramCalle, paramNumero, paramDepto, paramComentario,
    		 paramDirConflictiva, paramDirConflictivaComentario);
	 if (bizDelegate.setModAddrBook(modDir))
		 paramUrl += "&mje_ok=1";
	 else
		 paramUrl += "&mje_ok=2";
		 
     
     // 4. Redirecciona salida
     res.sendRedirect(paramUrl);

 }//execute

}
