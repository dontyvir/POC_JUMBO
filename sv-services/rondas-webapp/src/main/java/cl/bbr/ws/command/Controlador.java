package cl.bbr.ws.command;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import cl.bbr.ws.common.framework.Autenticador;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.pedidos.dto.ActDetallePickingDTO;
import cl.bbr.jumbocl.pedidos.dto.FOSustitutosCriteriosDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductosPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.RondaDTO;
import cl.bbr.jumbocl.pedidos.dto.TPBinOpDTO;
import cl.bbr.jumbocl.pedidos.dto.TPDetallePedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.TPDetallePickingDTO;
import cl.bbr.jumbocl.pedidos.dto.TPRegistroPickingDTO;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.usuarios.dto.LocalDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.ws.bizdelegate.BizDelegate;
import cl.bbr.ws.dto.DTOPicking;
import cl.bbr.ws.exceptions.WsException;


/**
 * Realiza el procesamiento de la carga y descarga de rondas con un usuario autentificado.
 */
public class Controlador {

	private Hashtable productos_ronda 	= new Hashtable();
	private List usuarios 			= new ArrayList();
	private List codigos_barra 		= new ArrayList();
	private String tipo_flujo		= "N";
	private List promo_producto     = new ArrayList();
	private List sust_criterios     = new ArrayList();
	
	//	---------- mod_ene09 - ini------------------------
	private String tipo_picking		= "N";
	//	---------- mod_ene09 - fin------------------------
	//	---------- mod_mar09 - ini------------------------
	public static final int PDA_PERFIL_PICK = 1;
	public static final int PDA_PERFIL_FISC = 2;
	public static final int PDA_PERFIL_BODG = 3;
    public static final int PDA_PERFIL_SUST = 4;
	//	---------- mod_mar09 - fin------------------------
	
	private Logging logger = new Logging(this);
	
	/**
	 * Constructor
	 */
	public Controlador(){
	}

	/**
	 * Método de carga de ronda. Utiliza los métodos de negocio.
	 * @param id_ronda
	 * @param login
	 * @param password
	 * @param tipo_aut 1:bd 2:ldap
	 * @throws WsException
	 * @throws SystemException
	 * @throws WsException
	 */
	public void doCargaRonda(long id_ronda, String login, String password, int tipo_aut)
		throws WsException, SystemException{
		
		// Creamos al BizDelegate
		BizDelegate biz = new BizDelegate();
		
		// obtiene ronda
		RondaDTO ronda;
		logger.debug("...doCargaRonda: " + id_ronda);
		ronda = biz.getRondaById(id_ronda);


		logger.debug("id_ronda: " + id_ronda);
		
		// en caso que no exista la ronda
		if ( ronda == null ){
			logger.info("La ronda no existe");
			throw new WsException("WSE001");
		}

		logger.debug("id_estado: " + ronda.getId_estado());
		
		// Valida estado de la ronda
		if ( ronda.getId_estado() != Constantes.ID_ESTADO_RONDA_CREADA ){
			logger.info("Sólo se permite cargar una ronda en estado Creada");
			throw new WsException("WSE002");
		}
		
		if(tipo_aut==Constantes.PDA_MODO_AUTENTICACION_BD){
			// autentifica usuario bsae de datos cuna
			if ( biz.doAutenticaUser(login, password) == false)
			{
				logger.info("Autenticación del usuario fallida");
				throw new WsException("WSE003");
			}
		}
		else if (tipo_aut==Constantes.PDA_MODO_AUTENTICACION_LDAP){
		    //autentica via ldap
		    Autenticador auth = new Autenticador();
		    boolean res=false;
		    try {
	    		//res =auth.doAuth(login, pass);
	    		res =auth.doLDAPAuth(login, password);
	    	} catch(Exception e){
	    	    logger.info("Autenticación del usuario vía LDAP fallida");
	    	    throw new WsException("WSE003");
	    	}
	    	logger.debug("Autenticacion LDAP [login="+login+"]:"+res);
	    	if (!res){
	    	    logger.info("Login o Password vía LDAP No Reconocido");
	    	    throw new WsException("WSE003");
	    	}
		}		
				
		// cargamos al usuario
		UserDTO user = biz.getUserByLogin(login);
		
		//busqueda del local de ronda en la lista de locals del usuario
		List lst_locales = user.getLocales();
		boolean encontro = false;
		for(int i=0; i<lst_locales.size();i++){
			LocalDTO loc = (LocalDTO)lst_locales.get(i);
			if(loc.getId_local()==ronda.getId_local()){
				encontro = true;
				//carga el tipo de flujo Parcial(sin bodega) o Normal
				this.tipo_flujo = loc.getTipo_flujo();
				break;
			}
		}
				
		// verificamos que el usuario pertenezca al local de la ronda
		//if ( user.getId_local() != ronda.getId_local() ){
		if ( !encontro){
			logger.info("Usuario no corresponde a local de la ronda");
			throw new WsException("WSE004");
		}
				
		//	---------- mod_ene09 - ini------------------------
		//obtiene del tipo de picking de la ronda
		this.tipo_picking = ronda.getTipo_picking();
		
		//		---------- mod_mar09 - ini------------------------		
		
		// Req 15: limpieza de usuarios en la carga
		// solo se deja al pickeador que realiza la carga, se fuerza el perfil	
		// se sacan los bodegueros del listado de picking
		

		boolean es_pickeador = biz.ValidaUserByPerfilByLocal(user.getId_usuario(), Constantes.ID_PERFIL_PICKEADOR, ronda.getId_local());

		if(!es_pickeador){
			throw new WsException("WSE007");
		}
		    user.setId_perfil(PDA_PERFIL_PICK);
		    this.usuarios.add(user);
		
			
		List listado = new ArrayList();

		listado = biz.getUsrByPerfilyLocal(Constantes.ID_PERFIL_FISCALIZADOR,ronda.getId_local());
		// traducimos el id_perfil con los indices de la aplicación portátil
		for (int i=0; i<listado.size();i++){
			UserDTO usr = new UserDTO();
			usr = (UserDTO)listado.get(i);
			//---------- mod_mar09 - ini------------------------	
			usr.setId_perfil(PDA_PERFIL_FISC);   // 2: Fiscalizador
			//---------- mod_mar09 - fin------------------------	
			this.usuarios.add(usr);
		}
		
        
        listado = biz.getUsrByPerfilyLocal(Constantes.ID_PERFIL_AUDITOR_SUSTITUCION,ronda.getId_local());
        // traducimos el id_perfil con los indices de la aplicación portátil
        for (int i=0; i<listado.size();i++){
            UserDTO usr = new UserDTO();
            usr = (UserDTO)listado.get(i);
            //---------- mod_mar09 - ini------------------------    
            usr.setId_perfil(PDA_PERFIL_SUST);   // 4: Auditor Sustitucion
            //---------- mod_mar09 - fin------------------------    
            this.usuarios.add(usr);
        }

        
		listado.clear();
		listado = biz.getSustitutosCriterios();
		// se traspasan los criterios
		for (int i=0; i<listado.size();i++){
			FOSustitutosCriteriosDTO sc = (FOSustitutosCriteriosDTO)listado.get(i);
			this.sust_criterios.add(sc);
		}
		
		// obtenemos productos de la ronda
		listado.clear();
		listado = biz.getProductosRonda(id_ronda);
		for (int i=0; i<listado.size();i++){
			ProductosPedidoDTO prod = new ProductosPedidoDTO();
			
			prod = (ProductosPedidoDTO)listado.get(i);

			logger.debug("->" + prod.getDescripcion() + " " + prod.getCant_spick());
			
			if ( prod.getPesable() != null && prod.getPesable().equalsIgnoreCase("S") )
				prod.setPesable("1");
			else
				prod.setPesable("0");
			this.productos_ronda.put(prod.getId_detalle() + "", prod);
			
		}
		
		// obtenemos codigos de barra de la ronda
		this.codigos_barra = biz.getBarrasRondaDetallePedido(id_ronda);
		
		// Obtenemos las promociones para los productos
		this.promo_producto = biz.getPromocionRonda(id_ronda);
		
		// Cambiamos el estado a la Ronda a En Picking y asignamos pickeador
		biz.doAsignaRonda(id_ronda,user.getId_usuario());
		
		logger.debug("ok Ctrl doCargaRonda");
		
	}


	/**
	 * Metodo para la descarga de rondas
	 * @param dtoDescarga
	 * @return mensaje
	 * @throws SystemException
	 * @throws WsException 
	 * @throws WsException
	 */
	public String doDescargaRonda(DTOPicking dtoDescarga)		
		throws SystemException, WsException{
	
		Document docDetallePedido 	      = dtoDescarga.getTablaDetallePedidoXML();
		Document docDetallePicking 	      = dtoDescarga.getTablaDetalle_PickingXML();
		Document docBin_Op 	              = dtoDescarga.getTablaBin_OpXML();
		Document docTablaRegistro_Picking = dtoDescarga.getTablaRegistro_PickingXML();
		int NRonda = 0;

		//System.out.println("docDetallePedido:\n=================\n" + docDetallePedido.toString());
		//System.out.println("docDetallePicking:\n==================\n" + docDetallePicking.toString());
		//System.out.println("docBin_Op:\n==========\n" + docBin_Op.toString());
		//System.out.println("docTablaRegistro_Picking:\n========================\n" + docTablaRegistro_Picking.toString());
		
		
		ActDetallePickingDTO dto = new ActDetallePickingDTO();
		//dto.setId_ronda(id_ronda);
		
		//Crear Bins******************
		List lst_bin = new ArrayList();
		TPBinOpDTO bin1;
		
		//Se extraen los valores de las tabla BinOp de descarga
        Element raizBin_Op = docBin_Op.getRootElement();
        List nBin_Op =  raizBin_Op.getChildren("bin_op");
        Iterator bop = nBin_Op.iterator();
	        
        while (bop.hasNext())
        {
        	bin1 = new TPBinOpDTO();
        	Element i = (Element)bop.next();
	        bin1.setPosicion(Integer.parseInt(i.getChild("posicion").getText()));
			bin1.setCod_bin( i.getChild("cod_bin").getText()+"" );
			bin1.setId_op(Integer.parseInt(i.getChild("id_op").getText()));
			bin1.setTipo(i.getChild("tipo").getText()+"");
			bin1.setCod_sello1( i.getChild("cod_sello1").getText()+"" );
			bin1.setCod_sello2( i.getChild("cod_sello2").getText()+"" );
			bin1.setCod_ubicacion(i.getChild("cod_ubicacion").getText()+"");
			//	---------- mod_feb09 - ini------------------------
			bin1.setVisualizado(i.getChild("visualizado").getText()+"");

			lst_bin.add(bin1);

		    logger.debug("\n\n  Descarga Bin_Op  -  posicion: " + i.getChild("posicion").getText()
			        + "  cod_bin:  " + i.getChild("cod_bin").getText()
			        + " id_op:  " + i.getChild("id_op").getText()
					+ " tipo:  " + i.getChild("tipo").getText()
					+ " cod_sello:  " + i.getChild("cod_sello1").getText()
					+ "	cod_sello2:  " + i.getChild("cod_sello2").getText()
					+ " cod_ubicacion:  " + i.getChild("cod_ubicacion").getText()
		    		+ " visualizado:  " + i.getChild("visualizado").getText());
			//	---------- mod_feb09 - fin------------------------
        }
	    
		dto.setLst_bin_op(lst_bin);
		logger.debug("Construyo el bin -> " + lst_bin.size());
		//Fin Crear Bins***************
			
		
		//Crear detalle_pedido***************
		List lst_det_pedido = new ArrayList();
		TPDetallePedidoDTO detP1; 
		
		//Se extraen los valores de las tabla DetPedido de descarga
        Element raizDetPedido = docDetallePedido.getRootElement();
        List nDetallePedido =  raizDetPedido.getChildren("detalle_pedido");    
        Iterator d = nDetallePedido.iterator();
        
        while (d.hasNext()){
	        Element i = (Element)d.next();
	        detP1 = new TPDetallePedidoDTO ();
			detP1.setId_detalle(Long.parseLong(i.getChild("id_detalle").getText()));//id_dronda en lugar de id_detalle
			detP1.setId_op(Long.parseLong(i.getChild("id_op").getText()));
			detP1.setCod_sap(i.getChild("cod_sap").getText()+"");
			detP1.setU_med(i.getChild("u_med").getText());
			detP1.setDescripcion(i.getChild("descripcion").getText());
			detP1.setCant_pedida(Double.parseDouble(i.getChild("cant_pedida").getText()));
			detP1.setCant_pickeada(Double.parseDouble(i.getChild("cant_pickeada").getText()));
			detP1.setCant_faltante(Double.parseDouble(i.getChild("cant_faltante").getText()));
			detP1.setCant_sinpickear(Double.parseDouble(i.getChild("cant_sinpickear").getText()));
			detP1.setPrecio(Double.parseDouble(i.getChild("precio").getText()));
			detP1.setObservacion(i.getChild("observacion").getText()+"");
			detP1.setEs_pesable(Integer.parseInt(i.getChild("es_pesable").getText()));
			detP1.setSector(i.getChild("sector").getText()+"");
			detP1.setSust_camb_form(Integer.parseInt(i.getChild("sust_camb_form").getText()));
			detP1.setId_dronda(Long.parseLong(i.getChild("id_dronda").getText()));
			detP1.setMot_sustitucion(Integer.parseInt(i.getChild("mot_sust").getText()));
			lst_det_pedido.add(detP1);
			
            logger.debug("\n\n  Descarga Detalle_Pedido \n\n id_detalle: " + i.getChild("id_detalle").getText()    
                    + "   id_op: " + i.getChild("id_op").getText()
                    + "   cod_sap: " + i.getChild("cod_sap").getText()
                    + "   u_med: " + i.getChild("u_med").getText()
                    + "   descripcion: " + i.getChild("descripcion").getText()
                    + "   cant_pedida: " + i.getChild("cant_pedida").getText()
                    + "   cant_pickeada: " + i.getChild("cant_pickeada").getText()
                    + "   cant_faltante: " + i.getChild("cant_faltante").getText()
                    + "   cant_sinpickear: " + i.getChild("cant_sinpickear").getText()
                    + "   precio: " + i.getChild("precio").getText()
                    + "   observacion: " + i.getChild("observacion").getText()
                    + "   es_pesable: " + i.getChild("es_pesable").getText()
                    + "   sector: " + i.getChild("sector").getText()
                    + "   sust_camb_form" + i.getChild("sust_camb_form").getText() 
                    + "   mot_sust: " + i.getChild("mot_sust").getText()    );
        }
    
		dto.setLst_det_pedido(lst_det_pedido);			
		logger.debug("Construyo los detalles del pedido -> " + lst_det_pedido.size());
        //Fin Crear detalle_pedido***************		
		
		
		//Crear detalle de picking***************
		List lst_det_pick = new ArrayList();
		TPDetallePickingDTO det1;
		
		//Se extraen los valores de las tabla DetPicking de descarga
        Element raizDetPicking = docDetallePicking.getRootElement();
        List nDetallePicking =  raizDetPicking.getChildren("detalle_picking");    
        Iterator dpi = nDetallePicking.iterator();
    
        while (dpi.hasNext()){
	        det1 = new TPDetallePickingDTO ();
          	Element i = (Element)dpi.next();
			det1.setCBarra(i.getChild("cbarra").getText());
			det1.setTipo(i.getChild("tipo").getText());
			det1.setId_detalle(Long.parseLong(i.getChild("id_detalle").getText()));//id_dronda en lugar de id_detalle
			det1.setPosicion(Integer.parseInt(i.getChild("posicion").getText()));
			det1.setId_dronda(Long.parseLong(i.getChild("id_dronda").getText()));
			det1.setCantidad(Double.parseDouble(i.getChild("cant").getText()));
			//	---------- mod_ene09 - ini------------------------
			//flag de audito S / N por cada producto pickeado 
			det1.setAuditado(i.getChildText("auditado"));
			logger.debug("Descarga Detalle Picking  cbarra:  " + i.getChildText("cbarra")
                    + "   tipo: " + i.getChildText("tipo")
                    + "   id_detalle: " + i.getChildText("id_detalle")
                    + "   posicion: " + i.getChildText("posicion")
                    + "   cant: " + i.getChildText("cant")
					+ "   auditado: " + i.getChildText("auditado")
					);			
			lst_det_pick.add(det1);
			//	---------- mod_ene09 - fin------------------------
          }			
			
        dto.setLst_det_picking(lst_det_pick);
        logger.debug("Construyo los detalles de picking  -> " + lst_det_pick.size());
		
		//Fin Crear detalle de picking******************
			
			
		//crear un log de registro picking 
		List lst_reg_pick = new ArrayList();
		TPRegistroPickingDTO reg = new TPRegistroPickingDTO ();

		Element raizRegistro_Picking = docTablaRegistro_Picking.getRootElement();
        List nRegistro_Picking =  raizRegistro_Picking.getChildren("registro_picking");
        Iterator rp = nRegistro_Picking.iterator();
      
        while (rp.hasNext())
        {
        	reg = new TPRegistroPickingDTO ();
        	Element i = (Element)rp.next();
            
        	int cons = Integer.parseInt(i.getChild("perfil").getText());

            reg.setUsuario(i.getChild("usuario").getText());
			if(cons == 1)					
				reg.setPerfil(Constantes.ID_PERFIL_PICKEADOR);
			else if(cons == 2)
				reg.setPerfil(Constantes.ID_PERFIL_FISCALIZADOR);
			else if(cons == 3)
				reg.setPerfil(Constantes.ID_PERFIL_BODEGUERO);
			
			reg.setRonda(Integer.parseInt(i.getChild("ronda").getText()));
			reg.setHora(i.getChild("hora").getText());
			
			//	---------- mod_ene09 - ini------------------------
			reg.setUsuario_fiscalizador("");
			reg.setE1(0);
			reg.setE2(0);
			reg.setE3(0);
			reg.setE4(0);
			reg.setE5(0);
			reg.setE6(0);
			reg.setE7(0);
			if((i.getChildText("usuario_fiscalizador")!=null) && (!i.getChildText("usuario_fiscalizador").equals(""))){ 
				reg.setUsuario_fiscalizador(i.getChildText("usuario_fiscalizador"));
				logger.debug("fiscalizador:"+i.getChildText("usuario_fiscalizador"));
			}
			if((i.getChildText("e1")!=null)&&(!i.getChildText("e1").equals(""))){ 
				reg.setE1(Integer.parseInt(i.getChildText("e1")));
				logger.debug("e1:"+i.getChildText("e1"));
			}
			if((i.getChildText("e2")!=null)&&(!i.getChildText("e2").equals(""))){
				reg.setE2(Integer.parseInt(i.getChildText("e2")));
				logger.debug("e2:"+i.getChildText("e2"));
			}
			if((i.getChildText("e3")!=null)&&(!i.getChildText("e3").equals(""))){
				reg.setE3(Integer.parseInt(i.getChildText("e3")));
				logger.debug("e3:"+i.getChildText("e3"));
			}
			if((i.getChildText("e4")!=null)&&(!i.getChildText("e4").equals(""))){
				reg.setE4(Integer.parseInt(i.getChildText("e4")));
				logger.debug("e4:"+i.getChildText("e4"));
			}
			if((i.getChildText("e5")!=null)&&(!i.getChildText("e5").equals(""))){
				reg.setE5(Integer.parseInt(i.getChildText("e5")));
				logger.debug("e5:"+i.getChildText("e5"));
			}
			if((i.getChildText("e6")!=null)&&(!i.getChildText("e6").equals(""))){
				reg.setE6(Integer.parseInt(i.getChildText("e6")));
				logger.debug("e6:"+i.getChildText("e6"));
			}
			if((i.getChildText("e7")!=null)&&(!i.getChildText("e7").equals(""))){
				reg.setE7(Integer.parseInt(i.getChildText("e7")));
				logger.debug("e7:"+i.getChildText("e7"));
			}			
						
			//	---------- mod_ene09 - fin------------------------			
			
			lst_reg_pick.add(reg);
			
			logger.debug("\n\n  Descarga Registro Picking  -  \n\n  Usuario: " + i.getChild("usuario").getText()    
	                  + "\n Perfil: " + i.getChild("perfil").getText()
	                  + "\n Ronda: " + i.getChild("ronda").getText()
	                  + "\n Hora: " + i.getChild("hora").getText());
			
			NRonda = Integer.parseInt(i.getChild("ronda").getText());	 
			
        }
            
        
		dto.setLst_reg_picking(lst_reg_pick);			
		logger.debug("Construyo el log del registro -> " + lst_reg_pick.size() );

		dto.setId_ronda(NRonda);
		
		// Creamos al BizDelegate
		BizDelegate biz = new BizDelegate();
		
		try {
			biz.recepcionRondaByPOS(dto);
		} catch (WsException e) {
			if ( e.getMessage().equals(Constantes._EX_RON_ID_INVALIDO) ){
				throw new WsException("WSE001"); //ronda no existe
			}
			else if ( e.getMessage().equals(Constantes._EX_RON_ESTADO_INADECUADO) ){
				throw new WsException("WSE002"); //ronda en estado incorrecto
			}
			else
				throw new WsException("WSE005"); //error no controlado
		} catch (SystemException e) {
			throw new WsException("WSE005"); //error no controlado
		}
		return "Descarga Completada!!";
	}

        
	/*--------------------------- Getters -----------------------------*/
	/**
	 * get usuarios
	 */
	public List getUsuarios() {
		return usuarios;
	}
	/**
	 * get codigos_barra
	 */
	public List getCodigos_barra() {
		return codigos_barra;
	}
	/**
	 * get productos_ronda
	 */
	public Hashtable getProductos_ronda() {
		return productos_ronda;
	}

	/**
	 * @return Devuelve sust_criterios.
	 */
	public List getSust_criterios() {
		return sust_criterios;
	}
	/**
	 * @param sust_criterios El sust_criterios a establecer.
	 */
	public void setSust_criterios(List sust_criterios) {
		this.sust_criterios = sust_criterios;
	}
	/**
	 * @return Returns the tipo_flujo.
	 */
	public String getTipo_flujo() {
		return tipo_flujo;
	}


	/**
	 * @return Devuelve promo_producto.
	 */
	public List getPromo_producto() {
		return promo_producto;
	}
	//	---------- mod_ene09 - ini------------------------	
	/**
	 * @return Devuelve tipo_pick.
	 */
	public String getTipo_picking() {
		return tipo_picking;
	}
	//	---------- mod_ene09 - fin------------------------	
}
