package cl.bbr.jumbocl.contenidos.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FichaNutricionalDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String cabecera;
	private final String item;
	private final String descripcion;
	private final String descripcion2;
	private final int order;
	private final int idProductoFO;
	private final List itemList;

	public static class Builder {

		private String cabecera;
		private String item;
		private String descripcion;
		private String descripcion2;
		private int order;
		private int idProductoFO;
		private List itemList = new ArrayList();

		public Builder(String ite) {
			this.item = ite;
		}
		
		public Builder(List lista) {
			this.itemList = lista;
		}

		public Builder(int id, String ite, List lista) {
			this.idProductoFO = id;
			this.item = ite;
			this.itemList = lista;
		}
		
		public Builder(int id, String ite) {
			this.idProductoFO = id;
			this.item = ite;
		}

		public FichaNutricionalDTO build() {
			FichaNutricionalDTO dto = new FichaNutricionalDTO(this);
			validateUserObject(dto);
			return dto;
		}

		private void validateUserObject(FichaNutricionalDTO dto) {
			if (dto == null) {
				throw new IllegalStateException("User object is null.");
			}
			if (dto.getIdProductoFO() == 0) {
				throw new IllegalArgumentException(
						"IdProductoFO debe ser mayot a cero.");
			}
			itemList(dto);
		}

		public Builder order(int ord) {
			this.order = ord;
			return this;
		}

		public Builder descripcion(String des) {
			this.descripcion = des;
			return this;
		}

		public Builder descripcion2(String des) {
			this.descripcion2 = des;
			return this;
		}
		
		public Builder cabecera(String cabecera) {
			this.cabecera = cabecera;
			return this;
		}
		

		public Builder itemList(FichaNutricionalDTO dto) {
			this.itemList.add(dto);
			return this;
		}

	}

	private FichaNutricionalDTO(Builder builder) {
		this.idProductoFO = builder.idProductoFO;
		this.item = builder.item;
		this.descripcion = builder.descripcion;
		this.descripcion2 = builder.descripcion2;
		this.order = builder.order;
		this.cabecera = builder.cabecera;
		this.itemList = builder.itemList;
	}

	public int getIdProductoFO() {
		return idProductoFO;
	}

	public String getItem() {
		return item;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public String getDescripcion2() {
		return descripcion2;
	}

	public int getOrder() {
		return order;
	}
	
	public String getCabecera() {
		return cabecera;
	}
	
	public List getItemList() {
		return itemList;
	}

	public String toString() {
		return "FichaNutricionalDTO [item=" + item + ", descripcion="
				+ descripcion + ", descripcion2=" + descripcion2 + ", order="
				+ order + ", idProductoFO=" + idProductoFO + "]";
	}

}
