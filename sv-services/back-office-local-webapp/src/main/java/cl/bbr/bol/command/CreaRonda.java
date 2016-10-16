package cl.bbr.bol.command;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.bol.exceptions.BolException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.dto.CreaRondaDTO;
import cl.bbr.jumbocl.pedidos.dto.IdPedidoCantDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Comando que Crea una ronda de picking para un local en un sector determinado
 * @author jsepulveda
 */
public class CreaRonda extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {


		
		// 1. Variables del método
		
		// 1.1 parámetro Url
		String 	paramUrl		= "";
		String 	paramId_sector 	= "";
		String	paramId_jornada	= "";
		long	id_sector 	= -1L;
		List 	indices 	= new ArrayList();
		long	id_jornada	= -1L;
		long    id_ronda = -1L;
		String  paramTipo_ve ="";

		
		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");
		//Recupera pagina desde web.xml
		String UrlError = getServletConfig().getInitParameter("UrlError");
		logger.debug("UrlError: " + UrlError);
		
		String MsjeError = getServletConfig().getInitParameter("MsjeError");
		MsjeError += " " + Constantes.CANT_MAX_PED_X_RONDA;
		logger.debug("MsjeError: " + MsjeError );
		
		// 2.1 Parámetro Url
		if ( req.getParameter("url") == null ){ throw new ParametroObligatorioException("url es null"); 	}
		paramUrl = req.getParameter("url");
		logger.debug("url: " + paramUrl);
		
		// 2.2 Parámetro id_sector
		if ( req.getParameter("id_sector") == null ){ throw new ParametroObligatorioException("id_sector es null"); }
		paramId_sector = req.getParameter("id_sector");
		logger.debug("id_sector: " + paramId_sector.replaceAll("\\?tipo_ve=",""));
		id_sector = Long.parseLong(paramId_sector.replaceAll("\\?tipo_ve=",""));
		
		// 2.3 Parámetro id_jornada
		if ( req.getParameter("id_jornada") == null ){ throw new ParametroObligatorioException("id_jornada es null");}
		paramId_jornada = req.getParameter("id_jornada");
		logger.debug("id_jornada: " + paramId_jornada);
		id_jornada = Long.parseLong(paramId_jornada);
		
		// 2.4 Tipo VE N:Normal o S:Especial
		if ( req.getParameter("tipo_ve") == null ){ 
			throw new ParametroObligatorioException("id_jornada es null");
		}
		paramTipo_ve = req.getParameter("tipo_ve");
		logger.debug("tipo_ve: " + paramTipo_ve);		
		
		// 2.5 Procesa los parámetros restantes
		Enumeration e = req.getParameterNames();
		//PrintWriter out = res.getWriter ();
		while (e.hasMoreElements()) {
			String name = (String)e.nextElement();
			if (name.startsWith("check_")){
				if (req.getParameter(name).equals("on")){
					String n = name.replaceFirst("check_","");
					indices.add(n);					
				}
			}
		}		
	
		// iteramos sobre los indices para crear mapa
		List lista = new ArrayList();
		for (int i=0; i<indices.size(); i++){
			//out.println("<br>i:" + (String)indices.get(i));
			String idx = (String)indices.get(i);
			String str_id_pedido 	= req.getParameter("id_pedido_" + idx);
			String str_cant			= req.getParameter("cant_" + idx);
			String str_cant_prod	= req.getParameter("cant_prod_" + idx);
			
			long id_pedido 	= Long.parseLong(str_id_pedido);
			double cant		= Double.parseDouble(str_cant);		
			double cant_prod	= Double.parseDouble(str_cant_prod);
			
			IdPedidoCantDTO dto = new IdPedidoCantDTO();			
			dto.setId_pedido(id_pedido);
			dto.setCant(cant);
			dto.setCant_prod(cant_prod);
			lista.add(dto);
		}
		
		// 3. Procesamiento Principal
		
		BizDelegate biz	= new BizDelegate();
		
		CreaRondaDTO rondanueva = new CreaRondaDTO();	
		
		rondanueva.setId_sector(id_sector);
		rondanueva.setId_jpicking(id_jornada);
		logger.debug("total de la lista : "+lista.size());
		
		rondanueva.setPedidos(lista);
		rondanueva.setId_local(usr.getId_local());
		rondanueva.setLst_indices(indices);
		rondanueva.setTipo_ve(paramTipo_ve);
		
		//biz.CreaRonda(id_sector, id_jornada, lista);
		ForwardParameters fp = new ForwardParameters();
		fp.add( req.getParameterMap() );
		try{
				id_ronda = biz.CreaRonda(rondanueva);
				
		
		}catch(BolException ex){
			if(ex.getMessage().equals(Constantes._EX_PED_ID_INVALIDO)){
				logger.debug("Id del pedido es inválido");
				fp.add( "rc" , Constantes._EX_PED_ID_INVALIDO);
				paramUrl = UrlError + fp.forward();
			}
			else if(ex.getMessage().equals(Constantes._EX_RON_ID_INVALIDO)){
				logger.debug("Id de la ronda es inválido");
				fp.add( "rc" , Constantes._EX_RON_ID_INVALIDO);
				paramUrl = UrlError + fp.forward();
			}
			else if(ex.getMessage().equals(Constantes._EX_RON_MAX_PEDIDOS_X_RONDA)){
				logger.debug("No se pudo crear la Ronda, pedidos superan los "+Constantes._EX_RON_MAX_PEDIDOS_X_RONDA);
				fp.add( "rc" , Constantes._EX_RON_MAX_PEDIDOS_X_RONDA);
				fp.add("msje",MsjeError);
				paramUrl = UrlError + fp.forward();
			}
			else if(ex.getMessage().equals(Constantes._EX_RON_SIN_PEDIDOS_SELEC)){
				logger.debug("no hay pedidos seleccionados");
				fp.add( "rc" , Constantes._EX_RON_SIN_PEDIDOS_SELEC);
				fp.add("msje",MsjeError);
				paramUrl = UrlError + fp.forward();
			}
			else if(ex.getMessage().equals(Constantes._EX_RON_PEDIDO_NO_COINCIDE)){
				logger.debug("Pulso el boton atras");
				fp.add( "rc" , Constantes._EX_RON_PEDIDO_NO_COINCIDE);
				fp.add("msje","No es posible crear la ronda debido a que pulso el boton Atras del Internet Explorer");
				paramUrl = UrlError + fp.forward();
			}
		}
		// 4. Redirecciona salida
		boolean vieneOtrosParametros=false;
		String simbolo="?";
		for (int x=0;x<paramUrl.length();x++){
			//si tiene simbolo de pregunta 
				if (simbolo.equalsIgnoreCase(String.valueOf(paramUrl.charAt(x)))){
					vieneOtrosParametros=true;
					break;
				}
		}
		if (vieneOtrosParametros)	   
			res.sendRedirect(paramUrl+"&tipo_ve="+paramTipo_ve+"&id_jornada="+id_jornada+"&id_ronda="+id_ronda+"&mjeError="+MsjeError);
		else
			res.sendRedirect(paramUrl+"?tipo_ve="+paramTipo_ve+"&id_jornada="+id_jornada+"&id_ronda="+id_ronda+"&mjeError="+MsjeError);
	
	}	
	
}
