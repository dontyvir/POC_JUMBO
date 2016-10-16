package cl.bbr.jumbocl.pedidos.ctrl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.pedidos.exceptions.PedidosException;
import junit.framework.TestCase;

public class PedidosCtrlTest extends TestCase {
	
	/*
    public void testSendEmailByPedido() throws PedidosException, SystemException {

        PedidosCtrl pedidosCtrl = new PedidosCtrl();
        pedidosCtrl.sendEmailByPedido(693436);        
    }
    */

    public void testSendEmailByPedido() throws PedidosException, SystemException {
    	
    	
    	PedidosCtrl pedidosCtrl = new PedidosCtrl();
    	
    	long op =1183939;//1084486 LA Florida
        //Tatiana:1099125, FERNANDO: 1120981, Stewart: 1129568
        //long op = 1136437;
       	pedidosCtrl.sendEmailByPedido(op);
       	
       	/*
    	
        String  ip_bd="172.18.150.35";
        String  puerto_bd="50000";
        String  instancia_bd="JMCL_VE";
        String  usu_bd="bodba";
        String  pwd_bd="bodba";
			
		try {
			Class.forName("com.ibm.db2.jcc.DB2Driver"); 
			Connection connection = DriverManager.getConnection("jdbc:db2://"+ip_bd+":"+puerto_bd+"/"+instancia_bd+"", usu_bd, pwd_bd);
			
		    ResultSet resultSet = null;
	        Statement statement = null;
	        
	    	statement = connection.createStatement();  
			String sql= "SELECT ID_PEDIDO, ID_ESTADO FROM BO_PEDIDOS WHERE FCREACION > '2013-12-31' AND ID_ESTADO=51 with ur";
		
            resultSet = statement.executeQuery(sql); 
            PedidosCtrl pedidosCtrl = new PedidosCtrl();
            while (resultSet.next()) {  
            	long op =resultSet.getLong("ID_PEDIDO");
               	System.out.println("ID_PEDIDO ::: "+op);
                //Tatiana:1099125, FERNANDO: 1120981, Stewart: 1129568
    	        //long op = 1136437;
               	pedidosCtrl.sendEmailByPedido(op);
               	
               	sql="SELECT FSM_ID, FSM_SUBJECT, FSM_DATA FROM FODBA.FO_SEND_MAIL WHERE FSM_ID = (select max (FSM_ID)  FROM FODBA.FO_SEND_MAIL) with ur";
                Statement stm =  connection.createStatement();
               	ResultSet rs = stm.executeQuery(sql);
               	if (rs.next()){
               		
               		String nombreArchivo= "C:\\EMIAL_HTML\\"+op+".html"; 
               		FileWriter fw = null;
               		try { 
               			fw = new FileWriter(nombreArchivo); 
               			BufferedWriter bw = new BufferedWriter(fw); 
               			PrintWriter salArch = new PrintWriter(bw); 

               			salArch.print(rs.getString("FSM_DATA")); 
               			salArch.close(); 
               		} 
               		catch (Exception ex) { 
               			System.out.println(ex.getMessage());
               		} 
               	}    
               	
               	rs.close();
    			stm.close();	
            }
             
			resultSet.close();
			statement.close();			
			connection.close();
	        
		}catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

	*/
        
    }

}


