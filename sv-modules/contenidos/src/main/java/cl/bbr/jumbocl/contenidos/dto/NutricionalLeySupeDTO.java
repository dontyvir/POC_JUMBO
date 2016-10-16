package cl.bbr.jumbocl.contenidos.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NutricionalLeySupeDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private final char excesoAzuraces;
	private final char excesoCalorias;
	private final char excesoGrasasSaturadas;
	private final char excesoSodio;
	private final int idProductoFO;	
	
	public static class Builder {
		private static final char PUBLICAR =  'P';
		private List excesos = null;
		
		private char excesoAzuraces = 'D';
		private char excesoCalorias = 'D';
		private char excesoGrasasSaturadas = 'D';
		private char excesoSodio = 'D';
		private int idProductoFO = 0;		
			
		public Builder(List lista, int idProducto ){
			
			this.idProductoFO = idProducto;
			
			Iterator ite = lista.iterator();
			String valor = null;
			while(ite.hasNext()){
				valor = (String)ite.next();
				
				if(getArrayExcesos().contains(valor)){
					
					if(valor.equals("EA")){
						this.excesoAzuraces = PUBLICAR;
						continue;
					}else  if(valor.equals("EC")){
						this.excesoCalorias = PUBLICAR;
						continue;
					}else if(valor.equals("EGS")){
						this.excesoGrasasSaturadas = PUBLICAR;
						continue;
					}else if(valor.equals("ES")){
						this.excesoSodio = PUBLICAR;
					}else{
						throw new IllegalArgumentException("Valores validos de imagen [EA, EC, EGS, ES]");
					}				
				}
			}
		}
		 
		public Builder(int id, char elem1, char elem2, char elem3, char elem4){
			this.idProductoFO = id;
			this.excesoAzuraces = elem1;
			this.excesoCalorias = elem2;
			this.excesoGrasasSaturadas = elem3;
			this.excesoSodio = elem4;			
		}

		private List getArrayExcesos(){
			excesos = new ArrayList();
			excesos.add("EA");
			excesos.add("EC");
			excesos.add("EGS");
			excesos.add("ES");
			return excesos;
		}
		
		public NutricionalLeySupeDTO build(){
			NutricionalLeySupeDTO dto =  new NutricionalLeySupeDTO(this);
			validateObject(dto);
			return dto;
		}
		private void validateObject(NutricionalLeySupeDTO dto) {
			 if(dto==null){
				 throw new IllegalArgumentException("Object [NutricionalLeySupeDTO] is null.");
			 }
			 if(dto.getIdProductoFO()==0){
				throw new IllegalArgumentException("IdProductoFO debe ser mayot a cero.");
			}		
		 }
	}
	
	 private NutricionalLeySupeDTO(Builder builder) {
		 this.excesoAzuraces = builder.excesoAzuraces;
		 this.excesoCalorias = builder.excesoCalorias;
		 this.excesoGrasasSaturadas = builder.excesoGrasasSaturadas;
		 this.excesoSodio = builder.excesoSodio;
		 this.idProductoFO = builder.idProductoFO;
	 }
	 
	 public char getExcesoAzuraces() {
		 return excesoAzuraces;
	 }

	 public char getExcesoCalorias() {
		 return excesoCalorias;
	 }

	 public char getExcesoGrasasSaturadas() {
		 return excesoGrasasSaturadas;
	 }

	 public char getExcesoSodio() {
		 return excesoSodio;
	 }
	 
	 public int getIdProductoFO() {
		 return idProductoFO;
	 }

	public String toString() {
		return "NutricionalLeySupeDTO [excesoAzuraces=" + excesoAzuraces
				+ ", excesoCalorias=" + excesoCalorias
				+ ", excesoGrasasSaturadas=" + excesoGrasasSaturadas
				+ ", excesoSodio=" + excesoSodio + ", idProductoFO="
				+ idProductoFO + "]";
	}
	 
}
