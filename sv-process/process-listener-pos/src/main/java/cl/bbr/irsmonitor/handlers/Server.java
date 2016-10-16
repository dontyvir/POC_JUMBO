/*
  (c) 2004, Nuno Santos, nfsantos@sapo.pt
  relased under terms of the GNU public license 
  http://www.gnu.org/licenses/licenses.html#TOCGPL
*/
package cl.bbr.irsmonitor.handlers;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ResourceBundle;
import java.util.Timer;

import cl.bbr.irsmonitor.command.Controlador;
import cl.bbr.irsmonitor.io.SelectorThread;


/**
 * A simple server for demonstrating the IO Multiplexing framework 
 * in action. After accepting a connection, it will read packets 
 * as defined by the SimpleProtocolDecoder class and echo them back. 
 * 
 * This server can accept and manage large numbers of incoming 
 * connections. For added fun remove the System.out statements and 
 * try it with several thousand (>10.000) clients. You might have to 
 * increase the maximum number of sockets allowed by the operating 
 * system.
 * 
 * @author Nuno Santos 
 */
public class Server implements AcceptorListener, PacketChannelListener {
    private SelectorThread st = null;  
    private int listenPort;
    private Acceptor acceptor = null;
    private Timer timer = null;
    static private ResourceBundle config;
  /**
   * Starts the server. 
   * 
   * @param listenPort The port where to listen for incoming connections.
   * @throws Exception
   */
    public Server() {
	    this.listenPort = -1;
    }  

    public Server(int listenPort) {
        this.listenPort = listenPort;
    }  
  
  
    public void start() throws Exception{
  	    if( this.listenPort == -1){
    	    this.listenPort = Integer.parseInt(Server.getConfig("server.port"));
  	    }
	    st = new SelectorThread();
	    acceptor = new Acceptor(listenPort, st, this);
	    acceptor.openServerSocket();
	    System.out.println("Listening on port: " + listenPort);
	    //timer = new Timer(true);
	    //timer.schedule(new IRSProcess(), 0, 60000);
    }

  
    public void stop(){
  	    try{
  	        if(timer != null)
   	            timer.cancel();
	        //if(acceptor != null)
            //    acceptor.close();
  	    } catch(Exception e){
  		    System.out.println("Error en finalizacion --> " + e.getMessage());
  	    }
        //if(st != null)
   	    //    st.requestClose();
    }

    //////////////////////////////////////////
    // Implementation of the callbacks from the 
    // Acceptor and PacketChannel classes
    //////////////////////////////////////////
    /**
     * A new client connected. Creates a PacketChannel to handle it.
     */
    public void socketConnected(Acceptor acceptor, SocketChannel sc) {    
        System.out.println("["+ acceptor + "] Socket connected: " + sc.socket().getInetAddress());
        try {
            // We should reduce the size of the TCP buffers or else we will
            // easily run out of memory when accepting several thousands of
            // connections
            sc.socket().setReceiveBufferSize(2*1024);
            sc.socket().setSendBufferSize(2*1024);
            // The contructor enables reading automatically.
            PacketChannel pc = new PacketChannel(sc, 
                                                 st, 
                                                 new IRSPosProtocolDecoder(), 
                                                 this);
            pc.resumeReading();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
  
    public void socketError(Acceptor acceptor, Exception ex) {
        System.err.println("["+ acceptor + "] Error: " + ex.getMessage());
    }

    //Funcion callback donde se procesa el mensaje.
    public void packetArrived(PacketChannel pc, ByteBuffer pckt) {
 
        //System.out.println("Packet received. : " + Utiles.buffer2string(pckt, pckt.remaining()));
        //System.out.println("Packet received. Size: " + pckt.remaining());
 	    
    	// Lógica del mensaje
  	    Controlador ctrl = new Controlador(pckt);
  	    ctrl.execute();
  	    String out = ctrl.getRespuesta();
  	    
  	    // Crea Buffer con la respuesta
  	    ByteBuffer sendMsg = generateNextPacket(out);
  	    
  	    // Envía paquete
  	    pc.sendPacket(sendMsg);

  	    
    }

    public void socketException(PacketChannel pc, Exception ex) {
        System.err.println("[" + (pc.getSocketChannel()).socket().getInetAddress() + "] Error: " + ex.getMessage());    
    }

    private ByteBuffer generateNextPacket(String sample) {
        // Generate a random size between 
        ByteBuffer buffer = ByteBuffer.allocate(sample.length());
        buffer.put(sample.getBytes());
        buffer.limit(buffer.position());
        buffer.flip();
  	    System.out.println("Sending packet. Size: " + buffer.remaining());
        return buffer;
    }
    
    public void socketDisconnected(PacketChannel pc) {
        System.out.println("[" + (pc.getSocketChannel()).socket().getInetAddress() + "] Disconnected.");
    }

    /**
     * The answer to a request was sent. Prepare to read the 
     * next request.
     */
  
    public void packetSent(PacketChannel pc, ByteBuffer pckt) {
        try {
            pc.resumeReading();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws Exception {
  	    //int listenPort = Integer.parseInt(args[0]);
  	    int listenPort = 4400;
        (new Server(listenPort)).start();
    }
    
    
    public boolean setConfiguration(ResourceBundle rc){
  	    config = rc;
  	    if (config.getString("server.port") == null )
    	    return false;
 
        return true;
    }
    
    
    public static String getConfig(String name){
   	    return config.getString(name);
    }	
}
