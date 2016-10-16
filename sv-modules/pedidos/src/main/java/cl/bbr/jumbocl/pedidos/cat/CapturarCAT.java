/*
 * Creado el 05-11-2010
 *
 */
package cl.bbr.jumbocl.pedidos.cat;

import java.util.ResourceBundle;

import cl.bbr.jumbocl.pedidos.util.FormatUtils;
import cl.bbr.jumbocl.shared.log.Logging;

/**
 * @author rsuarezr
 *
 */
public class CapturarCAT {

	   private static int MAX_SIZE 		= 2500;
	   private static String correlId 	= "0014";
	   private static String correlIdRSP = "@@@@@@@@@@@@@@@@@@@@@@@@";
	   private String qManager 			= "QM_TEST";
	   private int 	  timeout 			= 5000;
	   private String cola_requerim 	= "";
	   private String cola_respuesta 	= "";

	   //private Hashtable org;
	   
	   Logging logger = new Logging(this);

	/**
	 * 
	 */
	public CapturarCAT() {

        /*
	      MQEnvironment.hostname = "172.18.150.39";
	      MQEnvironment.channel = "QC_RF_RESERVA";
	      MQEnvironment.port = 1412;
	      timeout 			= 5000;
	      qManager 			= "QM_TEST";
	      cola_requerim 	= "QL_RF_RESERVA_REQ";
	      cola_respuesta	= "QL_RF_RESERVA_RSP";
        */
	       
	      ResourceBundle rb = ResourceBundle.getBundle("mq");


	      /*org = new Hashtable();
	      org.put("615290", "601");
	      org.put("900000", "601");
	      org.put("653020", "601");
	      org.put("910009", "611");
	      org.put("615280", "603");*/
	}
	
	public RptaCapturaCAT confirmar_reserva(SolicCapturaCAT solCAT) {
		byte[] messageId = send(solCAT);
		if (messageId == null)
	         return null;
		RptaCapturaCAT rptaCAT = receive(messageId);
	      return rptaCAT;
	}
	
	private byte[] send(SolicCapturaCAT solCAT) {
		return null;
	}
	
	private RptaCapturaCAT receive(byte[] mId) {
		return new RptaCapturaCAT();
		
	}
	
	public static void main(String[] args) throws Exception {
		
		//ResourceBundle rb = ResourceBundle.getBundle("D:/desarrollo/workspace_RB/ListenerPOS/src/bo.properties");
		
		/*HashMap rb = new HashMap();
		rb.put("CAT.numComercioAutenticado", "1581");
		rb.put("CAT.numComercioNoAutenticado", "1581");
        
        CAT.numComercioFOCliente=1580
        CAT.numComercioFOCliFono=1584
        CAT.numComercioFOVE=1583 
        
        **/
		
		/*JdbcPedidosDAO dao1 = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
		PedidoDTO pedido = dao1.getPedidoById( id_pedido );
		BotonPagoDTO bp = dao1.botonPagoGetByPedido(id_pedido);
		/*if (pedido.getId_usuario_fono() != 0 && pedido.getOrigen().equals("V")){
			//numComercio = rb.getString("CAT.numComercioNoAutenticado");
			numComercio = rb.get("CAT.numComercioNoAutenticado").toString();
		}else{
			//numComercio = rb.getString("CAT.numComercioAutenticado");
			numComercio = rb.get("CAT.numComercioAutenticado").toString();
		}*/
        
        
        /*
         Caso3-WB       6152902700008873 pide    25022 2528700 reservado
         CAso3- FOCO    6152902505253526 no pide 25022 2528800 reservado
         * */
        
        long id_pedido = 25288;
        int monto = 25022;
        String numComercio="1584"; //"1580";

		CapturarCAT capturar = new CapturarCAT();
		SolicCapturaCAT sol = new SolicCapturaCAT();
		sol.setNumempre(numComercio);
		String fecha = "11/02/2011";
		String ftrx = fecha.substring(6) + fecha.substring(3,5) + fecha.substring(0,2);
		sol.setFectrans(ftrx);
		sol.setCodtrans("FO02");
		//sol.setIdtrn(Formatos.completaCadena(String.valueOf(id_pedido), ' ', 16-(String.valueOf(id_pedido).length()),'D'));
		//sol.setIdtrn(FormatUtils.addCharToString(bp.getIdCatPedido() +"", " ", 16, FormatUtils.ALIGN_LEFT));
		sol.setIdtrn(FormatUtils.addCharToString(id_pedido +"00", " ", 16, FormatUtils.ALIGN_LEFT));
		sol.setTipoauto(" ");
		sol.setCodautor("        ");
		//sol.setMonconta(Formatos.completaCadena(String.valueOf(monto), '0', 9-(String.valueOf(monto).length()),'I'));
		sol.setMonconta(FormatUtils.addCharToString(monto+"", "0", 9, FormatUtils.ALIGN_RIGHT));
		sol.setHorenvio("163710");
        
        //El codigo de DPC de captura solo es para la confirmación de la reserva de cupo. 
        //En el caso de la anulación de la reserva enviar ese dato con ceros.
        sol.setDpc("000000004815");
        System.out.println("*** DPC LOCAL : " + sol.getDpc());
        
		try {
			RptaCapturaCAT rpta = capturar.confirmar_reserva(sol);
			if(rpta!=null) {
				System.out.println("rpta ver    :"+rpta.getVersion());
				System.out.println("rpta numemp :"+rpta.getNumempre());
				System.out.println("rpta fectrn :"+rpta.getFectrans());
				System.out.println("rpta codtrn :"+rpta.getCodtrans());
				System.out.println("rpta idtrn  :"+rpta.getIdtrn());
				System.out.println("rpta tipaut :"+rpta.getTipoauto());
				System.out.println("rpta codrpta:"+rpta.getCodrespu());
				System.out.println("rpta data   :"+rpta.getDatrespu());
				System.out.println("rpta codaut :"+rpta.getCodautoc());
				System.out.println("rpta horenv :"+rpta.getHorenvio());
				System.out.println("rpta nrouni :"+rpta.getNrounico());
				System.out.println("***********************************");
				System.out.println("***********************************");
				System.out.println("***  F I N   E J E C U C I Ó N  ***");
				System.out.println("***********************************");
				System.out.println("***********************************");
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
