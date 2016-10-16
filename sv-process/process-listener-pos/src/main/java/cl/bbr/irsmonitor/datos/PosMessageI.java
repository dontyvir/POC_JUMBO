package cl.bbr.irsmonitor.datos;

import java.nio.ByteBuffer;

public interface PosMessageI {
	public boolean getRequest(ByteBuffer msg);
	public ByteBuffer setResponse();
	public boolean process();
}
