package cl.cencosud.procesos;

public class Barra {

    private String codigoProducto;
    private String codigo;
    private String tipo;
    private String ppal;
    private String unidad;

    /**
     * @return Returns the codigo.
     */
    public String getCodigo() {
        return codigo;
    }

    public long getCodigoBigInt() {
        return codigo.length() > 1 ? Long.parseLong(codigo.substring(0, codigo.length() - 1)) : Long.parseLong(codigo);
    }

    /**
     * @param codigo
     *            The codigo to set.
     */
    public void setCodigo( String codigo ) {
        this.codigo = codigo;
    }

    /**
     * @return Returns the ppal.
     */
    public String getPpal() {
        return ppal;
    }

    /**
     * @param ppal
     *            The ppal to set.
     */
    public void setPpal( String ppal ) {
        this.ppal = ppal;
    }

    /**
     * @return Returns the tipo.
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @param tipo
     *            The tipo to set.
     */
    public void setTipo( String tipo ) {
        this.tipo = tipo;
    }

    /**
     * @return Returns the unidad.
     */
    public String getUnidad() {
        return unidad;
    }

    /**
     * @param unidad
     *            The unidad to set.
     */
    public void setUnidad( String unidad ) {
        this.unidad = unidad;
    }

    /**
     * @return Returns the codigoProducto.
     */
    public String getCodigoProducto() {
        return codigoProducto;
    }

    /**
     * @param codigoProducto
     *            The codigoProducto to set.
     */
    public void setCodigoProducto( String codigoProducto ) {
        this.codigoProducto = codigoProducto;
    }
}
