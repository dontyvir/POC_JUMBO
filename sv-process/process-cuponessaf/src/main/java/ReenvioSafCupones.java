import cl.bbr.jumbocl.pedidos.promos.interfaces.ClienteTcpPromosCuponesSaf;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;

public class ReenvioSafCupones {
	
	public static void main(String[] args) {
		
		JdbcTransaccion trx1 = new JdbcTransaccion();
		
		try {
			trx1.begin();
		} catch (Exception e) {
			System.err.println("Error al iniciar transacción!");

			e.printStackTrace();
		}
		
		try {
			
			ClienteTcpPromosCuponesSaf socketsaf = new ClienteTcpPromosCuponesSaf("192.168.50.23", 4600, trx1);
			socketsaf.EnviarSaf();
		
		} catch (Exception e) {
			
			System.err.println("Falla al enviar quema de cupones saf!");
			e.printStackTrace();
		
		}

		try {
			
			trx1.end();
		} catch (DAOException e) {
			
			System.err.println("Error al iniciar transacción!");

			e.printStackTrace();
		
		}
	
	}

}