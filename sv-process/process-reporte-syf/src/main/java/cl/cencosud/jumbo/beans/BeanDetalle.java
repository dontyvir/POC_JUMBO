package cl.cencosud.jumbo.beans;


public class BeanDetalle {

    private String local;
    private String idDetalle;
    private String catProd;
    private String codProd1;
    private String uniMed1;
    private String descripcion1;
    private String cantSolicitada;
    private String codProd2;
    private String uniMed2;
    private String descripcion2;
    private String cantPickeadaFaltante;
    private String idRonda;

    public BeanDetalle() {
    }

    public String getCantPickeadaFaltante() {
        return cantPickeadaFaltante;
    }

    public String getCantSolicitada() {
        return cantSolicitada;
    }

    public String getCatProd() {
        return catProd;
    }

    public String getCodProd1() {
        return codProd1;
    }

    public String getCodProd2() {
        return codProd2;
    }

    public String getDescripcion1() {
        return descripcion1;
    }

    public String getDescripcion2() {
        return descripcion2;
    }

    public String getIdDetalle() {
        return idDetalle;
    }

    public String getLocal() {
        return local;
    }

    public String getUniMed1() {
        return uniMed1;
    }

    public String getUniMed2() {
        return uniMed2;
    }

    public String getIdRonda() {
        return idRonda;
    }

    public void setCantPickeadaFaltante(String cantPickeadaFaltante) {
        this.cantPickeadaFaltante = cantPickeadaFaltante;
    }

    public void setCantSolicitada(String cantSolicitada) {
        this.cantSolicitada = cantSolicitada;
    }

    public void setCatProd(String catProd) {
        this.catProd = catProd;
    }

    public void setCodProd1(String codProd1) {
        this.codProd1 = codProd1;
    }

    public void setCodProd2(String codProd2) {
        this.codProd2 = codProd2;
    }

    public void setDescripcion1(String descripcion1) {
        this.descripcion1 = descripcion1;
    }

    public void setDescripcion2(String descripcion2) {
        this.descripcion2 = descripcion2;
    }

    public void setIdDetalle(String idDetalle) {
        this.idDetalle = idDetalle;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public void setUniMed1(String uniMed1) {
        this.uniMed1 = uniMed1;
    }

    public void setUniMed2(String uniMed2) {
        this.uniMed2 = uniMed2;
    }

    public void setIdRonda(String idRonda) {
        this.idRonda = idRonda;
    }

	public String toString() {
		return "BeanDetalle [local=" + local + ", idDetalle=" + idDetalle
				+ ", catProd=" + catProd + ", codProd1=" + codProd1
				+ ", uniMed1=" + uniMed1 + ", descripcion1=" + descripcion1
				+ ", cantSolicitada=" + cantSolicitada + ", codProd2="
				+ codProd2 + ", uniMed2=" + uniMed2 + ", descripcion2="
				+ descripcion2 + ", cantPickeadaFaltante="
				+ cantPickeadaFaltante + ", idRonda=" + idRonda + "]";
	}
}