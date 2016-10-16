package cl.bbr.fo.util;

import java.util.ResourceBundle;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.SystemException;
import cl.bbr.jumbocl.eventos.dto.EventoDTO;
import cl.bbr.jumbocl.eventos.dto.PasoDTO;


/**
 * 
 * Utilitarios
 * 
 * @author imoyano
 *
 */
public class UtilsEventos {

	/**
	 * Constructor
	 *
	 */
	public UtilsEventos() {
	}	
	
	/**
	 * Retorna el codigo del flash, cuando corresponde mostrar al cliente
	 * @param clienteRut Rut de Cliente
	 * @param paso Paso actual
	 * @return Codigo HTML
	 * @throws SystemException
	 */
	public static String codHtmlDeFlash(long clienteRut, int pasoNavegacion) throws SystemException {
	    String codFlash = "";
	    BizDelegate biz = new BizDelegate();
		EventoDTO evento = biz.getEventoMostrarByRut(clienteRut);
		boolean mostrarLayer = false;
		if (evento.getIdEvento() != 0) {
		    for (int i = 0; i < evento.getPasos().size(); i++) {
		        PasoDTO paso = (PasoDTO) evento.getPasos().get(i);
		        if (paso.getIdPaso() == pasoNavegacion) {
		            mostrarLayer = true;
		        }
		    }		    
		}			
		ResourceBundle rbEventos = ResourceBundle.getBundle("confEventos");
	    if (mostrarLayer) {
			String rutaFlash	= rbEventos.getString("dir_eventos");
	        String flashAncho	= rbEventos.getString("flash_ancho");
	        String flashAlto	= rbEventos.getString("flash_alto");
	        codFlash			= rbEventos.getString("codigo_html");
	    	codFlash 			= codFlash.replaceAll("@flash_ancho",flashAncho);
			codFlash 			= codFlash.replaceAll("@flash_alto",flashAlto);
			codFlash 			= codFlash.replaceAll("@nombre_archivo",rutaFlash+evento.getNombreArchivo());
			
	    }  
	    return codFlash;
	}
	
	/**
	 * Retorna el evento
	 * @param clienteRut Rut de Cliente
	 * @param paso Paso actual
	 * @return evento
	 * @throws SystemException
	 */
	public static EventoDTO eventoMostrado(long clienteRut) throws SystemException {
	    BizDelegate biz = new BizDelegate();
		EventoDTO evento = biz.getEventoMostrarByRut(clienteRut);
		return evento;
	}
	
    /**
     * Entrega el titulo con el nombre del cliente si corresponde (si tiene el parametro @nombre)
     * @param titulo Titulo guardado en la BD
     * @param nombre Nombre del cliente
     * @return titulo integrado con el nombre
     */
	public static String tituloMostrar(String titulo, String nombre) {
        if ( titulo.indexOf( "@nombre" ) != -1 ) {
            titulo = titulo.replaceAll("@nombre", nombre);
        }
        return titulo;
    }
	
		
}
