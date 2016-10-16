package cl.bbr.irsmonitor.handlers;

import java.io.IOException;
import java.nio.ByteBuffer;

import cl.bbr.irsmonitor.io.ProtocolDecoder;
import cl.bbr.jumbocl.shared.log.Logging;

final public class IRSPosProtocolDecoder implements ProtocolDecoder {

	private Logging logger = new Logging(this);
    private final static int HDR_BUFFER_SIZE = 56;
    
    private byte[] bufHeader = new byte[HDR_BUFFER_SIZE];
    private byte[] bufDetail = null;
    //private byte[] bufDetail; = new byte[BUFFER_SIZE * 100];
    private int posHdr = 0;
    private int posDet = 0;
    private int lenDet = 0;
    private boolean isHeader = true;
    
  
    public ByteBuffer decode(ByteBuffer socketBuffer) throws IOException {    
        // Reads until the buffer is empty or until a packet
        // is fully reassembled.
    	//logger.debug("inicio");
        while (socketBuffer.hasRemaining()) {
            // Copies into the temporary buffer
            byte b = socketBuffer.get();           
        	if( isHeader ){
                bufHeader[posHdr] = b;                
	            posHdr++;	      
	            if (posHdr == HDR_BUFFER_SIZE) {
	            	isHeader = false;
	            	String aux = new String(bufHeader,HDR_BUFFER_SIZE-4,4);
	            	try{
	               	    lenDet = Integer.parseInt(aux);
	            		bufDetail = new byte[lenDet];
	            	}catch(Exception e){
	                    throw new IOException("Error obteniendo largo del detalle");	
	            	}
	            }
        	}
        	else{
		        bufDetail[posDet] = b;
		        posDet++;
	            if (posDet == lenDet) {
	            	byte[] newBuffer = new byte[posHdr+posDet];
	    		    System.arraycopy(bufHeader, 0, newBuffer, 0, posHdr);
	    		    System.arraycopy(bufDetail, 0, newBuffer, posHdr, posDet);                
	    		    ByteBuffer packetBuffer = ByteBuffer.wrap(newBuffer);        
	    		    posHdr = 0;
	    		    posDet = 0;
	    		    lenDet = 0;
	    		    bufDetail = null;
	            	isHeader = true;
	    		    return packetBuffer;
	            }
        	}
        }
        // No packet was reassembled. There is not enough data. Wait
        // for more data to arrive.
        return null;
    }  
}