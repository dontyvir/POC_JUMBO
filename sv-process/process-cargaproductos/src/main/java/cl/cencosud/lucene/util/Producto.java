package cl.cencosud.lucene.util;

import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

/**
 * @author jdroguett
 *  
 */

public class Producto {

    private int idFo;

    private int idBo;

    private String codigo;

    private String tipo;

    private String descripcion;

    private String descripcionBOLarga;

    private String descripcionBOCorta;

    private String descripcionCompleta;

    private String unidadMedida;

    private String imagenMiniFicha;

    private BigDecimal cantidadEnEmpaque;

    //precios por local
    private Hashtable precios; // <localId, precio>

    //publicacion por local
    private Hashtable publicaciones; // <localId,

    //todas las categoria donde esta el producto
    private Set categorias; // <String>

    private int marcaId;

    private String marca;

    private BigDecimal cantidadMinima;

    private BigDecimal cantidadMaxima;

    private String unidadNombre;

    private boolean conNota;

    private String esParticionable;

    private long particion;

    private String categoria;

    private int categoriaId;

    private String[] stem;

    private HashMap tieneStock;

    private Set locales;
    
    private String descripcionLarga;

    public Producto() {
        this.precios = new Hashtable();
        this.publicaciones = new Hashtable();
        this.categorias = new HashSet();
        this.locales = new HashSet(); 
    }

    public void addCategoria(String categoria) {
        this.categorias.add(categoria);
    }

    public Set getCategorias() {
        return categorias;
    }

    public BigDecimal getPrecio(int localId) {
        return (BigDecimal) precios.get(new Integer(localId));
    }

    public Hashtable getPrecios() {
        return precios;
    }

    public void setPreciosCodeStr(String precios) {
        String[] aPrecios = precios.split("\\|");
        for (int i = 0; i < aPrecios.length; i += 2) {
            addPrecio(Integer.parseInt(aPrecios[i]), new BigDecimal(aPrecios[i + 1]));
        }
    }

    public String getPreciosCodeStr() {
        Enumeration enu = precios.keys();
        StringBuffer sb = new StringBuffer();
        while (enu.hasMoreElements()) {
            Integer local = (Integer) enu.nextElement();
            BigDecimal precio = (BigDecimal) precios.get(local);
            sb.append(local + "|" + precio);
            if (enu.hasMoreElements())
                sb.append("|");
        }
        return sb.toString();
    }

    public void setPublicacionesCodeStr(String publicaciones) {
        String[] aPublicaciones = publicaciones.split("\\|");
        for (int i = 0; i < aPublicaciones.length; i += 2) {
            addPublicado(Integer.parseInt(aPublicaciones[i]), "true".equals(aPublicaciones[i + 1]));
        }
    }

    public String getPublicacionesCodeStr() {
        Enumeration enu = publicaciones.keys();
        StringBuffer sb = new StringBuffer();
        while (enu.hasMoreElements()) {
            Integer local = (Integer) enu.nextElement();
            Boolean publicado = (Boolean) publicaciones.get(local);
            sb.append(local + "|" + publicado);
            if (enu.hasMoreElements())
                sb.append("|");
        }
        return sb.toString();
    }

    public boolean isPubAutocompletar() {
        Enumeration enu = publicaciones.elements();
        while (enu.hasMoreElements()) {
            Boolean pub = (Boolean) enu.nextElement();
            if (pub.booleanValue())
                return true;
        }
        return false;
    }

    public void addPrecio(int localId, BigDecimal precio) {
        this.precios.put(new Integer(localId), precio);
    }

    public boolean isPublicado(int localId) {
        return ((Boolean) publicaciones.get(new Integer(localId))).booleanValue();
    }

    public void addPublicado(int localId, boolean publicado) {
        Boolean pub = (Boolean) this.publicaciones.get(new Integer(localId));
        if (pub != null)
            this.publicaciones.put(new Integer(localId), new Boolean(publicado || pub.booleanValue()));
        else
            this.publicaciones.put(new Integer(localId), new Boolean(publicado));
    }

    public BigDecimal getCantidadMaxima() {
        return cantidadMaxima;
    }

    public void setCantidadMaxima(BigDecimal cantidadMaxima) {
        this.cantidadMaxima = cantidadMaxima;
    }

    public BigDecimal getCantidadMinima() {
        return cantidadMinima;
    }

    public void setCantidadMinima(BigDecimal cantidadMinima) {
        this.cantidadMinima = cantidadMinima;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public boolean isConNota() {
        return conNota;
    }

    public void setConNota(boolean conNota) {
        this.conNota = conNota;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEsParticionable() {
        return esParticionable;
    }

    public void setEsParticionable(String esParticionable) {
        this.esParticionable = esParticionable;
    }

    public int getIdFo() {
        return idFo;
    }

    public void setIdFo(int id) {
        this.idFo = id;
    }

    public int getIdBo() {
        return idBo;
    }

    public void setIdBo(int idBo) {
        this.idBo = idBo;
    }

    public String getImagenMiniFicha() {
        return imagenMiniFicha;
    }

    public void setImagenMiniFicha(String imagenMiniFicha) {
        this.imagenMiniFicha = imagenMiniFicha;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public int getMarcaId() {
        return marcaId;
    }

    public void setMarcaId(int marcaId) {
        this.marcaId = marcaId;
    }

    public long getParticion() {
        return particion;
    }

    public void setParticion(long particion) {
        this.particion = particion;
    }

    /*
     * public BigDecimal getPrecioPorUnidad() { try { if (cantidadEnEmpaque ==
     * null || cantidadEnEmpaque.equals(CERO)) return CERO; return
     * precio.divide(cantidadEnEmpaque, 2, BigDecimal.ROUND_HALF_UP); } catch
     * (Exception e) { System.out.println(cantidadEnEmpaque);
     * System.out.println(precio); throw new RuntimeException(); } }
     */

    public BigDecimal getCantidadEnEmpaque() {
        return cantidadEnEmpaque;
    }

    public void setCantidadEnEmpaque(BigDecimal valorEmpaque) {
        this.cantidadEnEmpaque = valorEmpaque.setScale(3, BigDecimal.ROUND_HALF_UP);
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public String getUnidadNombre() {
        return unidadNombre;
    }

    public void setUnidadNombre(String unidadNombre) {
        this.unidadNombre = unidadNombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String[] getStem() {
        return stem;
    }

    public void setStem(String[] stem) {
        this.stem = stem;
    }

    public String getDescripcionCompleta() {
        return descripcionCompleta;
    }

    public void setDescripcionCompleta(String descripcionCompleta) {
        this.descripcionCompleta = descripcionCompleta;
    }

    public int getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(int categoriaId) {
        this.categoriaId = categoriaId;
    }

    public HashMap getTieneStock() {
        return tieneStock;
    }

    public void setTieneStock(HashMap tieneStock) {
        this.tieneStock = tieneStock;
    }

    public String getDescripcionBOLarga() {
        return descripcionBOLarga;
    }

    public void setDescripcionBOLarga(String descripcionBOLarga) {
        this.descripcionBOLarga = descripcionBOLarga;
    }

    public String getDescripcionBOCorta() {
        return descripcionBOCorta;
    }

    public void setDescripcionBOCorta(String descripcionBOCorta) {
        this.descripcionBOCorta = descripcionBOCorta;
    }

    public Set getLocales() {
        return locales;
    }

    public void setLocales(Set locales) {
        this.locales = locales;
    }

    public Hashtable getPublicaciones() {
        return publicaciones;
    }

    public void setPublicaciones(Hashtable publicaciones) {
        this.publicaciones = publicaciones;
    }

    public void setCategorias(Set categorias) {
        this.categorias = categorias;
    }

    public void setPrecios(Hashtable precios) {
        this.precios = precios;
    }

    public String getDescripcionLarga() {
        return descripcionLarga;
    }
    public void setDescripcionLarga(String descripcionLarga) {
        this.descripcionLarga = descripcionLarga;
    }

     public String getTieneStockStr() {
		String str = null; 
		str = tieneStock.toString();
			str = str.replaceAll(" ", "");
			str = str.replaceAll("=", "|");
			int ini = str.indexOf("{")!=-1?str.indexOf("{")+1:0;
		int fin = str.indexOf("}")!=-1?str.indexOf("}"):str.length();
		str = str.substring(ini, fin);
		return str;
     }

     
     
}