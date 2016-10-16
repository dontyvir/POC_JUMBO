package cl.cencosud.informes.manual;

import java.io.IOException;


public class TestInformesManualBOC {
 
	   final static int DIA_INI			= 16;
	   final static int MES_INI			= 12;
	   final static int DIA_FIN			= 13;
	   final static int MES_FIN			= 01;
	   final static int AGNO_INI 			= 2013; 
	   final static int AGNO_FIN 			= 2014; 
	//   final static int FLAG_MANUAL	    = 0;
	   
	    public static void main(String[] args) {  
	    	TestInformesManualBOC ex = new TestInformesManualBOC();  
	    	ex.execute("/home/was/programas_consolas/informes/bin/informes_manual.sh");    
	    }  
	    public void execute(String path) {  

	           try {  
	        	   String yourShellInput = DIA_INI+" "+MES_INI+" "+DIA_FIN+" "+MES_FIN+" "+AGNO_INI+" "+AGNO_FIN;  // or whatever ... 
	        	   String[] commandAndArgs = new String[]{ path,yourShellInput };
	        	   Runtime.getRuntime().exec(commandAndArgs);
	   
	                } catch (IOException e) {  
	                       e.printStackTrace();  
	                }  
	            
	       } 

}
