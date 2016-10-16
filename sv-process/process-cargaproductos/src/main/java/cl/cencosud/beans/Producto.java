package cl.cencosud.beans;

import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * @author jdroguett
 * 
 */

public class Producto {
    private int id;
    private String codigoCategoria;
    private String codigo;
    private String codigo2;
    private String nombre;
    private String descripcion;
    private int estado;
    private String marca;
    private String codigoProPpal;
    private String origen;
    private String unidadBase;
    private String Ean13;
    private String unidadEmpaque;
    private String atributo9;
    private String atributo10;
    private String unidadMedida;

    //No se se usan, equivalen a num_conv y den_conv de la tabla bo_productos
    //private int numeradorConversion;

    //private int denominadorConversion;

    /**
     * @return Returns the atributo10.
     */
    public String getAtributo10() {
        return atributo10;
    }

    /**
     * @param atributo10
     *            The atributo10 to set.
     */
    public void setAtributo10(String atributo10) {
        this.atributo10 = atributo10;
    }

    /**
     * @return Returns the atributo9.
     */
    public String getAtributo9() {
        return atributo9;
    }

    /**
     * @param atributo9
     *            The atributo9 to set.
     */
    public void setAtributo9(String atributo9) {
        this.atributo9 = atributo9;
    }

    /**
     * @return Returns the catagoria_id.
     */
    public String getCodigoCategoria() {
        return codigoCategoria;
    }

    /**
     * @param catagoria_id
     *            The catagoria_id to set.
     */
    public void setCodigoCategoria(String catagoria_id) {
        this.codigoCategoria = catagoria_id;
    }

    /**
     * @return Returns the codigo.
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * @param codigo
     *            The codigo to set.
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * @return Returns the codigo2.
     */
    public String getCodigo2() {
        return codigo2;
    }

    /**
     * @param codigo2
     *            The codigo2 to set.
     */
    public void setCodigo2(String codigo2) {
        this.codigo2 = codigo2;
    }

    /**
     * @return Returns the codigoProPpal.
     */
    public String getCodigoProPpal() {
        return codigoProPpal;
    }

    /**
     * @param codigoProPpal
     *            The codigoProPpal to set.
     */
    public void setCodigoProPpal(String codigoProPpal) {
        this.codigoProPpal = codigoProPpal;
    }

    /**
     * @return Returns the descripcion.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion
     *            The descripcion to set.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return Returns the ean13.
     */
    public String getEan13() {
        return Ean13;
    }

    /**
     * @param ean13
     *            The ean13 to set.
     */
    public void setEan13(String ean13) {
        Ean13 = ean13;
    }

    /**
     * @return Returns the estado.
     */
    public int getEstado() {
        return estado;
    }

    /**
     * @param estado
     *            The estado to set.
     */
    public void setEstado(int estado) {
        this.estado = estado;
    }

    /**
     * @return Returns the marca.
     */
    public String getMarca() {
        return marca;
    }

    /**
     * @param marca
     *            The marca to set.
     */
    public void setMarca(String marca) {
        this.marca = marca;
    }

    /**
     * @return Returns the nombre.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre
     *            The nombre to set.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return Returns the origen.
     */
    public String getOrigen() {
        return origen;
    }

    /**
     * @param origen
     *            The origen to set.
     */
    public void setOrigen(String origen) {
        this.origen = origen;
    }

    /**
     * @return Returns the unidadBase.
     */
    public String getUnidadBase() {
        return unidadBase;
    }

    /**
     * @param unidadBase
     *            The unidadBase to set.
     */
    public void setUnidadBase(String unidadBase) {
        this.unidadBase = unidadBase;
    }

    /**
     * @return Returns the unidadEmpaque.
     */
    public String getUnidadEmpaque() {
        return unidadEmpaque;
    }

    /**
     * @param unidadEmpaque
     *            The unidadEmpaque to set.
     */
    public void setUnidadEmpaque(String unidadEmpaque) {
        this.unidadEmpaque = unidadEmpaque;
    }

    /**
     * @return Returns the id.
     */
    public int getId() {
        return id;
    }

    /**
     * @param id
     *            The id to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return Returns the unidadMedida.
     */
    public String getUnidadMedida() {
        return unidadMedida;
    }

    /**
     * @param unidadMedida
     *            The unidadMedida to set.
     */
    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    /**
     * Comparacion simple
     */

    public boolean equals(Object object) {
        if (!(object instanceof Producto)) {
            return false;
        }
        Producto producto = (Producto) object;
        return new EqualsBuilder().append(this.codigo2, producto.codigo2).append(this.nombre, producto.nombre)
                .append(this.descripcion, producto.descripcion).append(this.estado, producto.estado).append(this.codigoCategoria, producto.codigoCategoria).isEquals();
    }
}