package cl.bbr.irsmonitor.datos;

import java.nio.ByteBuffer;

import cl.bbr.irsmonitor.dto.C1ReqDTO;
import cl.bbr.irsmonitor.dto.HeaderPosDTO;
import cl.bbr.irsmonitor.utils.Utiles;

public class C1Message extends PosMessage implements PosMessageI{
	
	private HeaderPosDTO 	header;
	private C1ReqDTO 		c1req;
	
	public C1Message(HeaderPosDTO hdr){
		this.header = new HeaderPosDTO();
		this.header = hdr;
		c1req = new C1ReqDTO();
		
	}

	/**
	 * Procesa y parsea el buffer para dejarlo en un objeto C1ReqDTO
	 */
	public boolean getRequest(ByteBuffer msg) {

		c1req.setOp(Utiles.buffer2string(msg,C1ReqDTO.OP_LEN));
		//System.out.println("N°OP: " + c1req.getOp());
		
		return true;
	}

	// Procesamiento principal
	public boolean process() {

	//	Controlador ctrl = new Controlador();
	
		
		
		
		return false;
	}

	public ByteBuffer setResponse() {
		return null;
	}

}
