
package cl.cencosud.beans;

/**
 * @author jdroguett
 *
 */
public class Local {
    private int id;
    private String codigo;
    private String nombre;

    /**
     * @return Returns the codigo.
     */
    public String getCodigo() {
        return codigo;
    }
    /**
     * @param codigo The codigo to set.
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    /**
     * @return Returns the id.
     */
    public int getId() {
        return id;
    }
    /**
     * @param id The id to set.
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * @return Returns the nombre.
     */
    public String getNombre() {
        return nombre;
    }
    /**
     * @param nombre The nombre to set.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
