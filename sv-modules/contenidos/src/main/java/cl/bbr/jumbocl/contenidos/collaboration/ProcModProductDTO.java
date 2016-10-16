package cl.bbr.jumbocl.contenidos.collaboration;

import java.io.Serializable;

public class ProcModProductDTO implements Serializable{
	private final static long serialVersionUID = 1;
		
	private long id_producto; // obligatorio
	private String tipo; 
	private String marca; 
	private String descr_corta; 
	private String desc_larga; 
	private String umedida; 
	private double contenido; 
	private String admite_com; 
	private String preparable; 
	private String uni_vta;
	private double interv_med; 
	private double maximo; 
	private String img1; 
	private String img2;
	
	private String imgBannerProducto; 
	private String descBannerProducto;
	private String colBannerProducto;
	        
	private long id_usr;
	private String val_dif;
	private String usr_login;
	private String mensaje;
	private String esParticionable;
	private Integer particion;
	
	/**
	 * True. Indica que se debe evitar la publicación o despublicación de estado de bloqueo. Es decir, mantener el estado actual del producto 
	 */
	private boolean evitarPubDes;
	
	/**
	 * True. Indica que el producto esta disponible para Grability
	 */
	private boolean publicadoGrability;
      

	/**
	 * @return Returns the id_usr.
	 */
	public long getId_usr() {
		return id_usr;
	}
	/**
	 * @param id_usr The id_usr to set.
	 */
	public void setId_usr(long id_usr) {
		this.id_usr = id_usr;
	}
	/**
	 * @param id_producto
	 * @param tipo
	 * @param marca
	 * @param descr_corta
	 * @param desc_larga
	 * @param umedida
	 * @param contenido
	 * @param admite_com
	 * @param preparable
	 * @param interv_med
	 * @param maximo
	 */
	public ProcModProductDTO(long id_producto, String tipo, String marca, String descr_corta, String desc_larga, 
			String umedida, double contenido, String admite_com, String preparable, double interv_med, double maximo) {
		super();
		this.id_producto = id_producto;
		this.tipo = tipo;
		this.marca = marca;
		this.descr_corta = descr_corta;
		this.desc_larga = desc_larga;
		this.umedida = umedida;
		this.contenido = contenido;
		this.admite_com = admite_com;
		this.preparable = preparable;
		this.interv_med = interv_med;
		this.maximo = maximo;
	}
	/**
	 * 
	 */
	public ProcModProductDTO() {
	}
	/**
	 * @param id_producto
	 * @param tipo
	 * @param marca
	 * @param descr_corta
	 * @param desc_larga
	 * @param umedida
	 * @param contenido
	 * @param admite_com
	 * @param preparable
	 * @param interv_med
	 * @param maximo
	 * @param img1
	 * @param img2
	 */
	
	
	public ProcModProductDTO(long id_producto, String tipo, String marca, String descr_corta, String desc_larga, 
			String umedida, double contenido, String admite_com, String preparable, double interv_med, double maximo, 
			String img1, String img2, String val_dif, long id_usr, String usr_login, String mensaje) {
		this.id_producto = id_producto;
		this.tipo = tipo;
		this.marca = marca;
		this.descr_corta = descr_corta;
		this.desc_larga = desc_larga;
		this.umedida = umedida;
		this.contenido = contenido;
		this.admite_com = admite_com;
		this.preparable = preparable;
		this.interv_med = interv_med;
		this.maximo = maximo;
		this.img1 = img1;
		this.img2 = img2;
		this.id_usr = id_usr;
		this.val_dif = val_dif;
		this.usr_login = usr_login;
		this.mensaje = mensaje;
	}
	/**
	 * @return Returns the admite_com.
	 */
	public String getAdmite_com() {
		return admite_com;
	}
	/**
	 * @return Returns the contenido.
	 */
	public double getContenido() {
		return contenido;
	}
	/**
	 * @return Returns the desc_larga.
	 */
	public String getDesc_larga() {
		return desc_larga;
	}
	/**
	 * @return Returns the descr_corta.
	 */
	public String getDescr_corta() {
		return descr_corta;
	}
	/**
	 * @return Returns the id_producto.
	 */
	public long getId_producto() {
		return id_producto;
	}
	/**
	 * @return Returns the img1.
	 */
	public String getImg1() {
		return img1;
	}
	/**
	 * @return Returns the img2.
	 */
	public String getImg2() {
		return img2;
	}
	/**
	 * @return Returns the interv_med.
	 */
	public double getInterv_med() {
		return interv_med;
	}
	/**
	 * @return Returns the marca.
	 */
	public String getMarca() {
		return marca;
	}
	/**
	 * @return Returns the maximo.
	 */
	public double getMaximo() {
		return maximo;
	}
	/**
	 * @return Returns the preparable.
	 */
	public String getPreparable() {
		return preparable;
	}
	/**
	 * @return Returns the tipo.
	 */
	public String getTipo() {
		return tipo;
	}
	/**
	 * @return Returns the umedida.
	 */
	public String getUmedida() {
		return umedida;
	}
	/**
	 * @param admite_com The admite_com to set.
	 */
	public void setAdmite_com(String admite_com) {
		this.admite_com = admite_com;
	}
	/**
	 * @param contenido The contenido to set.
	 */
	public void setContenido(double contenido) {
		this.contenido = contenido;
	}
	/**
	 * @param desc_larga The desc_larga to set.
	 */
	public void setDesc_larga(String desc_larga) {
		this.desc_larga = desc_larga;
	}
	/**
	 * @param descr_corta The descr_corta to set.
	 */
	public void setDescr_corta(String descr_corta) {
		this.descr_corta = descr_corta;
	}
	/**
	 * @param id_producto The id_producto to set.
	 */
	public void setId_producto(long id_producto) {
		this.id_producto = id_producto;
	}
	/**
	 * @param img1 The img1 to set.
	 */
	public void setImg1(String img1) {
		this.img1 = img1;
	}
	/**
	 * @param img2 The img2 to set.
	 */
	public void setImg2(String img2) {
		this.img2 = img2;
	}
	/**
	 * @param interv_med The interv_med to set.
	 */
	public void setInterv_med(double interv_med) {
		this.interv_med = interv_med;
	}
	/**
	 * @param marca The marca to set.
	 */
	public void setMarca(String marca) {
		this.marca = marca;
	}
	/**
	 * @param maximo The maximo to set.
	 */
	public void setMaximo(double maximo) {
		this.maximo = maximo;
	}
	/**
	 * @param preparable The preparable to set.
	 */
	public void setPreparable(String preparable) {
		this.preparable = preparable;
	}
	/**
	 * @param tipo The tipo to set.
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	/**
	 * @param umedida The umedida to set.
	 */
	public void setUmedida(String umedida) {
		this.umedida = umedida;
	}
	/**
	 * @return Returns the uni_vta.
	 */
	public String getUni_vta() {
		return uni_vta;
	}
	/**
	 * @param uni_vta The uni_vta to set.
	 */
	public void setUni_vta(String uni_vta) {
		this.uni_vta = uni_vta;
	}
	/**
	 * @return Returns the mensaje.
	 */
	public String getMensaje() {
		return mensaje;
	}
	/**
	 * @param mensaje The mensaje to set.
	 */
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	/**
	 * @return Returns the usr_login.
	 */
	public String getUsr_login() {
		return usr_login;
	}
	/**
	 * @param usr_login The usr_login to set.
	 */
	public void setUsr_login(String usr_login) {
		this.usr_login = usr_login;
	}
	/**
	 * @return Returns the val_dif.
	 */
	public String getVal_dif() {
		return val_dif;
	}
	/**
	 * @param val_dif The val_dif to set.
	 */
	public void setVal_dif(String val_dif) {
		this.val_dif = val_dif;
	} 
      
    /**
     * @return Devuelve esParticionable.
     */
    public String getEsParticionable() {
        return esParticionable;
    }
    /**
     * @return Devuelve particion.
     */
    public Integer getParticion() {
        return particion;
    }
    /**
     * @param esParticionable El esParticionable a establecer.
     */
    public void setEsParticionable(String esParticionable) {
        this.esParticionable = esParticionable;
    }
    /**
     * @param particion El particion a establecer.
     */
    public void setParticion(Integer particion) {
        this.particion = particion;
    }
    /**
     * @return Returns the serialVersionUID.
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
    /**
     * @return Returns the evitarPubDes.
     */
    public boolean isEvitarPubDes() {
        return evitarPubDes;
    }
    /**
     * @param evitarPubDes The evitarPubDes to set.
     */
    public void setEvitarPubDes(boolean evitarPubDes) {
        this.evitarPubDes = evitarPubDes;
    }
	/**
	 * @return el imgBannerProducto
	 */
	public String getImgBannerProducto() {
		return imgBannerProducto;
	}
	/**
	 * @param imgBannerProducto el imgBannerProducto a establecer
	 */
	public void setImgBannerProducto(String imgBannerProducto) {
		this.imgBannerProducto = imgBannerProducto;
	}
	/**
	 * @return el descBannerProducto
	 */
	public String getDescBannerProducto() {
		return descBannerProducto;
	}
	/**
	 * @param descBannerProducto el descBannerProducto a establecer
	 */
	public void setDescBannerProducto(String descBannerProducto) {
		this.descBannerProducto = descBannerProducto;
	}
	
	/**
	 * @return el colBannerProducto
	 */
	public String getColBannerProducto() {
		return colBannerProducto;
	}
	/**
	 * @param colBannerProducto el colBannerProducto a establecer
	 */
	public void setColBannerProducto(String colBannerProducto) {
		this.colBannerProducto = colBannerProducto;
	}
	
	/**
	 * @return el publicadoGrability
	 */
	public boolean isPublicadoGrability() {
		return publicadoGrability;
	}
	/**
	 * @param publicadoGrability el publicadoGrability a establecer
	 */
	public void setPublicadoGrability(boolean publicadoGrability) {
		this.publicadoGrability = publicadoGrability;
	}
    
}