
import cl.cencosud.jumbo.home.TestTracking;


public class TestGeneraPlanillaTracking {
	
	public static void main(String[] args) {
		//carga_archivo();
	    TestTracking track = new TestTracking();
		track.inicia();
		
		try{
	    
		    /***************************************************/
	        /*GregorianCalendar date1 = new GregorianCalendar();
	        date1.set(2010, 02, 29, 8, 35, 00);

	        GregorianCalendar date2 = new GregorianCalendar();
	        date2.set(2010, 02, 30, 0, 5, 00);

	        long diff = date2.getTimeInMillis() - date1.getTimeInMillis();


	        //obtenemos los segundos
	        long segundos = diff / 1000;
	      
	        //obtenemos las horas
	        //long horas = segundos / 3600;
	      
	        //restamos las horas para continuar con minutos
	        //segundos -= horas*3600;
	      
	        //igual que el paso anterior
	        long minutos = segundos /60;
	        segundos -= minutos*60;
	      
	        //ponemos los resultados en un mapa :-)
	        //System.out.println("horas   : " + Long.toString(horas));
	        System.out.println("minutos : " + Long.toString(minutos));
	        System.out.println("segundos: " + Long.toString(segundos));*/

		}catch(Exception e){
		    e.printStackTrace();
		}

	}
}
