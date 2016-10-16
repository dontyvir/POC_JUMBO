package cl.bbr.jumbocl.pedidos.promos.cargas;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.pedidos.ctrl.LocalCtrl;
import cl.bbr.jumbocl.pedidos.dto.LocalDTO;
import cl.bbr.jumbocl.pedidos.exceptions.LocalException;
import cl.bbr.jumbocl.pedidos.promos.cargas.dto.HeaderFeedBackDTO;
import cl.bbr.jumbocl.pedidos.promos.cargas.dto.MensajeFeedbackDTO;
import cl.bbr.jumbocl.pedidos.promos.cargas.dto.TipoFb01ProductoDTO;
import cl.bbr.jumbocl.pedidos.promos.cargas.dto.TipoFb02SeccionDTO;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.promo.carga.ctrl.CargaCtrl;
import cl.bbr.promo.carga.dto.FeedbackDTO;
import cl.bbr.promo.carga.dto.FeedbackProductoDTO;
import cl.bbr.promo.carga.dto.FeedbackSeccionDTO;
import cl.bbr.promo.lib.helpers.Util;

public class CargaPromocion {
	Logging logger = new Logging(this);
	private String path_pd = "";
	private String path_fb = "";
	
	private String host_irs_promo = Constantes.PROMO_SERVER_HOST_FB;
	private int port_irs_promo = Constantes.PROMO_SERVER_FB_PORT;
		

	public CargaPromocion() {
	}
	
	/**
	 * Genera el mensajeFeedBack de las promociones
	 * @param feedback
	 * @return
	 */
	private MensajeFeedbackDTO generaMsg(FeedbackDTO feedback){
		MensajeFeedbackDTO resp = new MensajeFeedbackDTO();
		List excep = new ArrayList();
		List excep_msg = new ArrayList();
		FeedbackProductoDTO fb_pro;
		FeedbackSeccionDTO fb_sec;
		
		try{
			//procesa encabezado
			HeaderFeedBackDTO head = new HeaderFeedBackDTO();
			head.setTipo(""+feedback.getTipo());
			head.setLocal(""+feedback.getLocal());
			head.setTarea(""+feedback.getIdtarea());
			head.setEstado(""+feedback.getCodret());
			head.setNro_prod_proc(""+feedback.getProdProc());
			head.setNro_prod_nproc(""+feedback.getProdNoProc());
			head.setNro_prom_proc(""+feedback.getPromoProc());
			head.setNro_prom_nproc(""+feedback.getPromoNoProc());
			//AAAAMMDDHHMMSS		
			head.setFecha_ini(""+Formatos.frmFechaHoraByDateFull(feedback.getFinicio()));
			head.setFecha_fin(""+Formatos.frmFechaHoraByDateFull(feedback.getFtermino()));
			head.setNro_bloq(""+feedback.getBloque());
			head.setTot_bloq(""+feedback.getTotal());
			head.setReg_excep(""+feedback.getRegExcep());				
			resp.setHead(head);
			
			excep = feedback.getExcep();
			logger.debug("EXCEPCIONES = "+excep.size());
			
			for( int i=0; i<excep.size(); i++ ) {
				// se controla el feedback de producto
				if(feedback.getTipo()==1) {
					fb_pro = (FeedbackProductoDTO)excep.get(i);
					logger.debug("promo="+fb_pro.getPromocion()
							+"|prod="+fb_pro.getProducto()
							+"|codret="+fb_pro.getCodret());
					TipoFb01ProductoDTO dto1 = new TipoFb01ProductoDTO();
					dto1.setCodigo(""+fb_pro.getPromocion());
					dto1.setProducto(""+fb_pro.getProducto());
					dto1.setCod_ret(""+fb_pro.getCodret());
					dto1.setFiller("0");
					excep_msg.add(dto1);
				}
				else if(feedback.getTipo()==2) {
					fb_sec = (FeedbackSeccionDTO)excep.get(i);
					logger.debug("seccion="+fb_sec.getCodigo()
							+"|promo="+fb_sec.getPromocion()
							+"|codret="+fb_sec.getCodret());				
					TipoFb02SeccionDTO dto2 = new TipoFb02SeccionDTO();
					dto2.setCodigo(""+fb_sec.getCodigo());
					dto2.setPromocion(""+fb_sec.getPromocion());
					dto2.setCod_ret(""+fb_sec.getCodret());
					dto2.setFiller("0");
					excep_msg.add(dto2);
				}
			}
			resp.setExcep(excep_msg);
			logger.debug("mensaje generado:"+resp.toMsg());		
		} catch(Exception ex){
			logger.error("No puede generar el mensaje de feedback");
			ex.printStackTrace();
		}
		return resp;
	}
	
	/**
	 * Envia el MensajeFeedback al servidor de promociones
	 * @param msg
	 * @param id_tarea
	 * @param cod_sap
	 */
	private void enviaFeedback(MensajeFeedbackDTO msg, int id_tarea, String cod_sap_local){
		LocalCtrl locctrl = new LocalCtrl();
		LocalDTO local=null;
		String local_archivo;
		Socket skCliente=null;
		OutputStream os=null;
		String cadena="", largo="";
		
		try{
			String host =host_irs_promo;
			int puerto =port_irs_promo;
			
			//genera data
			local_archivo = cod_sap_local;			
			cadena = msg.toMsg();
			
			//Reemplaza el local sap por el local promociones
			local = locctrl.getLocalPromoByCodLocalSap(cod_sap_local);
			if(local!=null) {
				local_archivo = local.getCod_local_promo();
				logger.debug("Reemplaza local :"+cod_sap_local+" por local promociones :"+local_archivo);				
				cadena = cadena.replaceAll(cod_sap_local,local_archivo);
				//cadena = cadena.replaceAll("J514","C002");
				}
			else{
				logger.error("No se encuentra el local de IRS promociones para el local sap:"+cod_sap_local+" revise BO_LOCALES");
			}
			
			//crea el archivo con el local promocion y local sap de JMCL
			FileOutputStream fos = new FileOutputStream(
					path_fb+
					Constantes.CARGAS_PROMO_PREFIJO_ARCH_FB+
					id_tarea+"."+local_archivo+"."+cod_sap_local);
			
			DataOutputStream dos2 = new DataOutputStream(fos);
			dos2.writeBytes(cadena);
			
			// inicio socket
			logger.debug("genera conexion socket a "+host+":"+puerto);
			skCliente = new Socket();			
			skCliente.connect(new InetSocketAddress ( host, puerto),
					Constantes.CARGAS_PROMO_ARCH_FB_TIMEOUT_MS);
			
			if (skCliente.isConnected()){
				os = skCliente.getOutputStream();
				DataOutputStream dos = new DataOutputStream(os);
				
				logger.debug("enviando ["+cadena+"] largo:"+cadena.length());
				
				//primero debe enviar 6 bytes con el largo relleno de ceros
				largo="000000"+cadena.length();
				largo = largo.substring(largo.length()-6, largo.length());
				logger.debug("primer  mensaje con largo de data:"+largo);
				dos.writeBytes(largo);
				
				//segundo envia la data
				logger.debug("segundo mensaje con data ["+cadena+"] largo:"+cadena.length());				
				dos.writeBytes(cadena);
				logger.debug("feedback enviado");
				dos.flush();
				}
			// fin socket
			skCliente.close();
			logger.debug("conexion socket finalizada!!");
			
		}catch(Exception ex){
			logger.error("No puede enviar el mensaje de feedback:"+ex.getMessage());
			//ex.printStackTrace();
		}
	}
	
	/**
	 * Carga un archivo de promociones de un local dado 
	 * @param archivo contiene la ruta y el nombre del archivo a cargar
	 * @param cod_local_sap el codigo sap del local ej:"J512"
	 */
	private void cargaArchPromocionConLib(String archivo, String cod_local_sap){		
		//String localSAP = "J512";
		//String archivo = "C:/PD999999.DAT";
		try {

			// iniciliza ambiente de carga
			CargaCtrl carga = new CargaCtrl(archivo, cod_local_sap);
			
			// procesa archivo de promociones
			FeedbackDTO feedback = carga.procesa();
			
			logger.debug("------------------------------------");
			logger.debug("TAREA = "+feedback.getIdtarea());
			logger.debug("ESTADO = "+feedback.getEstado()+" | CODRET = "+feedback.getCodret());
			logger.debug("PROMO="+feedback.getPromoProc()+" !PROMO="+feedback.getPromoNoProc());
			logger.debug("SKU="+feedback.getProdProc()+" !SKU="+feedback.getProdNoProc());
			logger.debug("TIEMPO= "+Util.getDate(feedback.getFinicio())+"|"+Util.getDate(feedback.getFtermino()));
			
			//genera archivo de feedback con el FB+id_tarea.+cod local
			
			//enviar respuesta via tcp
			enviaFeedback(generaMsg(feedback), feedback.getIdtarea(), cod_local_sap);			
			//System.exit( 0 );
			
		}catch (Exception ex){
			logger.error("No puede cargar el archivo "+archivo+" del local "+cod_local_sap+" :"+ex.getMessage());
			ex.printStackTrace();
		}	
		
		
	}
	
	/**
	 * Obtiene el numero de local pos desde un archivo de promociones
	 * @param archivo
	 * @return
	 */
	private String extraeLocal(String archivo){
		
		LocalCtrl locctrl = new LocalCtrl();
		LocalDTO local=null;
		String resp=null;
		BufferedReader input=null;
		
		try {
			input =  new BufferedReader(new FileReader(archivo));
		
			
			String line = null;
			int lineas=0;
			/*
			* readLine is a bit quirky :
			* it returns the content of a line MINUS the newline.
			* it returns null only for the END of the stream.
			* it returns an empty String if two newlines appear in a row.
			*/
			while (( line = input.readLine()) != null){
				//ignora lineas cortas que tienen datos malformados
				//con esto no se cae la recuperacion de archivos				
				if(line.length()<21)
					continue;
				lineas++;
				// cada linea es de 248 bytes
				if (line.charAt(0)=='0'){//locales
					//se obtiene el primer local
					int cod_local_pos = Integer.parseInt(line.substring(16, 20));
					local = locctrl.getLocalSapByCodLocalPos(cod_local_pos);
					logger.debug("linea:"+lineas+" tipo="+line.charAt(0)+" cod_local_pos:"+cod_local_pos+" cod_local:"+local.getCod_local());
					resp = local.getCod_local();
				}
				else if (line.charAt(0)=='7'){//cheksum										
					if (lineas!=Integer.parseInt(line.substring(1, 7))){
						logger.error("linea:"+lineas+" tipo="+line.charAt(0)+" cheksum="+line.substring(1, 7)
								+" no es valido<>"+lineas);
						return null;
					}
				}
				//contents.append(line);
				//contents.append(System.getProperty("line.separator"));
			}
			
		} catch (IOException ex){			
			ex.printStackTrace();
			logger.error("No puede abrir el archivo de promociones:"+ex.getMessage());
			return null;
		} catch (LocalException e) {
			e.printStackTrace();
			logger.error("No puede encontrar el local de jumbo.cl:"+e.getMessage());
			return null;		
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("No puede leer el archivo:"+e.getMessage());
			return null;
		}finally {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
		
		
		return resp;
		
	}
	
	private void carga_prop(){
		String path_promos;
		String path_fback;
		String host;
		int port;
		
		InputStream props = this.getClass().getResourceAsStream("/carga_promo.properties");
		Properties prop = new Properties();
		try {
			prop.load(props);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		path_promos= prop.getProperty("path_pd");
		path_fback = prop.getProperty("path_fb");
		host=prop.getProperty("irs_promo.host");
		if (prop.getProperty("irs_promo.port")!=null)
			port=Integer.parseInt(prop.getProperty("irs_promo.port"));
		else
			port=-1;
		
		
		if ((path_promos!=null) && (!path_promos.equals(""))){
			System.out.println("lee path_PD = "+path_promos);
			path_pd= path_promos;
		}
		
		if ((host!=null) && (!host.equals(""))){
			System.out.println("lee irs promo host = "+host);
			host_irs_promo= host;
		}	
		if ((path_fback!=null) && (!path_fback.equals(""))){
			System.out.println("lee path_FB = "+path_fback);
			path_fb= path_fback;
		}	
		
		if (port>0){
			System.out.println("lee irs promo port = "+port);
			port_irs_promo= port;
		}	
		
		
	}
	
	
	/**
	 * - Busca archivos en el directorio de promociones
	 * - Obtiene el numero de local pos
	 * - Entrega el archivo a la libreria de carga
	 */
	public void inicia(){
		String local_sap;
		//cargaproperties
		carga_prop();
		
		// por cada archivo revisar el cod_local_pos
		
		File directorio=new File(path_pd);		
		
		//busca archivos en base al filtro
		File[] listaArchivos=directorio.listFiles(
				new FiltroArchPromo(
						Constantes.CARGAS_PROMO_PREFIJO_ARCH,
						Constantes.CARGAS_PROMO_EXT_ARCH));	
		
		//descarta el procesamiento si no encuentra archivos
		if ((listaArchivos==null)||(listaArchivos.length==0)){
			System.out.println("No existen archivos para procesar en "
					+path_pd
					+Constantes.CARGAS_PROMO_PREFIJO_ARCH+"*"
					+Constantes.CARGAS_PROMO_EXT_ARCH);
			logger.debug("No existen archivos para procesar en "
					+path_pd
					+Constantes.CARGAS_PROMO_PREFIJO_ARCH+"*"
					+Constantes.CARGAS_PROMO_EXT_ARCH);
			return;
		}
		
		logger.debug("numero de archivos:"+listaArchivos.length+" en "+path_pd );
        for(int i=0; i<listaArchivos.length; i++){
        	
        	//extrae nombre del archivo   	
            File fichero = listaArchivos[i];
                        
            //si el archivo existe y no es un directorio busca el proximo
            if ((fichero.isDirectory()) || (!fichero.exists()))
            	continue;
            
            logger.debug((i+1)+"-. archivo:"+fichero.getName());
               
            //extrae el local sap leyendo en el archivo encontrado
        	local_sap = extraeLocal(fichero.getAbsolutePath());        	
        	if ((local_sap==null) || (local_sap.equals("")) ){
            	//si no encuentra el codigo local sap marca en el errorlog el problema        		
        		logger.error("No puede obtener el codigo LOCAL SAP para el archivo "+fichero.getAbsolutePath());
        		continue;
        	}
        	//el local sap obtenido es
        	logger.debug((i+1)+"-. cod_local_sap:"+local_sap);
    		
        	//prepara la llamada al metodo de la libreria de carga
        	//CargaPromocion cargador = new CargaPromocion();
        	logger.debug((i+1)+"-. ------------inicio libreria carga --------");
        	//cargador.cargaArchPromocionConLib(fichero.getAbsolutePath(), local_sap);
        	cargaArchPromocionConLib(fichero.getAbsolutePath(), local_sap);
        	logger.debug((i+1)+"-. ------------fin    libreria carga --------");
        	
        	//Renombra archivo procesado y lo deja en el mismo directrio
        	String nom_salida=fichero.getAbsolutePath()+Constantes.CARGAS_PROMO_EXT_ARCH_PROC;
        	File fichero_salida = new File(nom_salida);        	
        	if (fichero_salida.exists()){
        	    fichero_salida.delete();
        	}        	
        	fichero.renameTo(new File(nom_salida));        	        	
        	logger.debug((i+1)+"-. procesado:"+nom_salida);
        }
    }
}


