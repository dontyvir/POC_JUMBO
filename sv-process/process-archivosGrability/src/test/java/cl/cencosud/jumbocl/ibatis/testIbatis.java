package cl.cencosud.jumbocl.ibatis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

public class testIbatis {
	
	static String separator = "|"; // Field separator in the file

	public static void main(String args[]){
		try {
			
			getAvailability();
			getCategoria();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("error = "+e.getMessage());
			e.printStackTrace();
		}
	}
	
	  public static void getAvailability() throws Exception{
		  
		  String resource = "connection.xml";
		  String timeStamp = new SimpleDateFormat("yyyyMMddkkmmss").format(new Date());
		  
		  //System.out.println(timeStamp);
		  
		  File outFile = new File("C:\\availability_"+timeStamp+".txt");
		  BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
	  
		  try{

			Reader reader = Resources.getResourceAsReader(resource);
			SqlMapClient sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
			
			//parametroBean pBean = new parametroBean();
			//pBean.setProd_id(76864);
			//pBean.setLoc_id(2);
			
//			System.out.println("Bean = "+pBean.getProd_id()+","+pBean.getLoc_id());
			
			Map hashCoche = (Map)sqlMap.queryForMap("getAvailability", "", "product_id");
			
			if(!hashCoche.isEmpty()){
				Iterator iter = hashCoche.keySet().iterator();

				while(iter.hasNext()){
					Integer key = (Integer) iter.next();
					Map hp = (Map)hashCoche.get(key);
					
/*					System.out.println("product_id = " + hp.get("product_id"));
					System.out.println("local_id = " + hp.get("local_id"));
					System.out.println("published = " + hp.get("published"));
					System.out.println("stock = " + hp.get("stock"));*/
					
					writer.write(hp.get("product_id")+separator+hp.get("local_id")+separator+hp.get("published")+separator+hp.get("stock"));
					writer.newLine(); // Esto es un salto de linea
					
					//System.out.println(hp.get("product_id")+separator+hp.get("local_id")+separator+hp.get("published")+separator+hp.get("stock"));
					
					//writer.write("Clave: " + key + " -> Valor: " + hashCoche.get(key));//retorna HashMap
					//writer.newLine(); // Esto es un salto de linea
				}
			}
			writer.close();

		      //return it;
		  }catch(Exception e){
		     // Log.error("Error al desplegar ID archivo",e);
			  System.out.println("error"+e);
			  //return it;
		  }
	  }
	  
	  public static void getCategoria() throws Exception{
		  
		  String resource = "connection.xml";
		  String timeStamp = new SimpleDateFormat("yyyyMMddkkmmss").format(new Date());
		  
		  //System.out.println(timeStamp);
		  
		  File outFile = new File("C:\\categoria_"+timeStamp+".txt");
		  BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		  
		  try{

			Reader reader = Resources.getResourceAsReader(resource);
			SqlMapClient sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
			
			Map mCountMap = (Map)sqlMap.queryForMap("getCategoria", "", "key", "value");
			
			if(!mCountMap.isEmpty()){
				Iterator iter = mCountMap.keySet().iterator();
				while(iter.hasNext()){
					Integer key = (Integer) iter.next();
					
					writer.write(key + separator + mCountMap.get(key));
					writer.newLine(); // Esto es un salto de linea
					
					//System.out.println("Clave: " + key + " -> Valor: " + mCountMap.get(key));
				}
			}
			writer.close();
		      //return it;
		  }catch(Exception e){
		     // Log.error("Error al desplegar ID archivo",e);
			  System.out.println("error"+e);
			  //return it;
		  }
	  }
	
}
