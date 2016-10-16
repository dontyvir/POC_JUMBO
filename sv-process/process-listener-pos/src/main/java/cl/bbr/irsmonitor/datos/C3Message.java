package cl.bbr.irsmonitor.datos;

import java.nio.ByteBuffer;

import cl.bbr.irsmonitor.dto.HeaderPosDTO;

public class C3Message extends PosMessage implements PosMessageI {

	
	public C3Message(HeaderPosDTO hdr){

	}

	public boolean getRequest(ByteBuffer msg) {
		return false;
	}

	public boolean process() {
		return false;
	}

	public ByteBuffer setResponse() {
		return null;
	}

}
