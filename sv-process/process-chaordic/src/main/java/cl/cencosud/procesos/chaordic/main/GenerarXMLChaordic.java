package cl.cencosud.procesos.chaordic.main;

import java.util.ArrayList;
import java.util.List;

import cl.cencosud.procesos.chaordic.ctrl.ChaordicCTRL;
import cl.cencosud.procesos.chaordic.dto.ConfigDTO;
import cl.cencosud.procesos.chaordic.log.Logging;

public class GenerarXMLChaordic {

	/**
	 * @param args
	 * @throws SystemException 
	 * @throws WsException 
	 */
	private Logging logger = new Logging(this);
	
	public static void main(String[] args) {
		GenerarXMLChaordic obj = new GenerarXMLChaordic();
		obj.execute(args);
	}
	
	public void execute(String[] args){
	
		List listaProductos = new ArrayList();
		String codigoLocal  = "1" ;

        // hora inicial para el calculo del tiempo total
        java.util.Date horaInicial = new java.util.Date();

        boolean todoOk = true;

        ChaordicCTRL chaordicCtrl = new ChaordicCTRL();
        ConfigDTO conf;
		
        try {
        	if (args.length > 0 && Long.parseLong(args[0]) > 0 ) {
        		codigoLocal = (args[0]);
        	}
        	
        	conf = chaordicCtrl.recuperarParametros();
        	
            // Recupera informacion de productos
        	listaProductos = chaordicCtrl.getListaProductosChaordic(conf,codigoLocal);
        	
        	// Genera archivo con datos obtenidos antes
        	chaordicCtrl.getArchivoXML(listaProductos,conf);
            
        } catch (Exception e) {
        	logger.error("ERROR " + e);
        	todoOk = false;
        }

        // FINALIZA proceso

        // calculo tiempo
        java.util.Date horaFinal = new java.util.Date();
        long dif = horaFinal.getTime() - horaInicial.getTime();
        long segundos = dif / 1000;
        long minutos = segundos / 60;
        segundos = segundos - minutos * 60;

        logger.info("-- RESUMEN GENERACION FEED.XML --");
        logger.info("termino OK: " + todoOk);
        logger.info("tiempo total: " + minutos + "m" + segundos + "s");

	}
}

