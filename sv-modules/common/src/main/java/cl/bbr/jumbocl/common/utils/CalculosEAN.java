package cl.bbr.jumbocl.common.utils;

/**
 * Clase que permite obtener el digito verificador de un Codigo de Barra.
 * @author bbr
 *
 */
public class CalculosEAN {

	
	public static String getEAN13dv(String cbarra){

		int a = 0;
		int b = 0;
		int dv = 0;

		if ( cbarra.length() > 12 ){
			cbarra = cbarra.substring(0,12);
		}
		

		for (int i=0; i<cbarra.length(); i++){
			if( i%2 == 1 ){
				a += Integer.parseInt( cbarra.substring(i,i+1) );
			}
			else{
				b += Integer.parseInt( cbarra.substring(i,i+1) );
			}
		}

		dv = 100 - (3*a+b);
		String str_dv = dv+"";

		String out = str_dv.substring(str_dv.length()-1);

		return out;

	}

}
