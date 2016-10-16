package cl.cencosud.test;

import java.util.List;

import cl.bbr.jumbocl.common.exceptions.ServiceException;
import cl.cencosud.jumbocl.hdp.dto.OutputFop;
import cl.cencosud.jumbocl.hdp.service.FopsService;
import junit.framework.TestCase;

public class ClienteTest  extends TestCase  {
	
	public void testGetFops(){
		
		
		 FopsService fps = new FopsService();		  
		 try {
			List fops =fps.getFops();
			for(int i=0; i<= fops.size(); i++){
					OutputFop oFops = 	(OutputFop) fops.get(i);
					System.out.println(oFops.toString());
			}
			
		} catch (Exception e) {
			// TODO Bloque catch generado automáticamente
			e.printStackTrace();
		}
	}
}
