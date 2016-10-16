/*
 * Created on 28-ago-2009
 */
package cl.cencosud.informes.datos;

import java.math.BigDecimal;

/**
 * @author jdroguett
 */
public class SustitutosYFaltantes {
   private String nombre;
   private BigDecimal pickeados;
   private BigDecimal sustituidos;
   private BigDecimal faltantes;
   private BigDecimal faltantesNoSustituir;

   public BigDecimal getFaltantes() {
      return faltantes;
   }

   public void setFaltantes(BigDecimal faltantes) {
      this.faltantes = faltantes;
   }

   public String getNombre() {
      return nombre;
   }

   public void setNombre(String nombre) {
      this.nombre = nombre;
   }

   public BigDecimal getPickeados() {
      return pickeados;
   }

   public void setPickeados(BigDecimal pickeados) {
      this.pickeados = pickeados;
   }

   public BigDecimal getSustituidos() {
      return sustituidos;
   }

   public void setSustituidos(BigDecimal sustituidos) {
      this.sustituidos = sustituidos;
   }

   public BigDecimal getTotal() {
      return pickeados.add(sustituidos).add(faltantes);
   }

   public BigDecimal getTotalFaltantes() {
      return faltantes.add(sustituidos);
   }

   public double getIndiceSustitucion() {
      return getTotalFaltantes().doubleValue() == 0d ? 0d : sustituidos.divide(getTotalFaltantes(),
            BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
   }

   public double getIndiceFaltantes() {
      return getTotal().doubleValue() == 0d ? 0d : getTotalFaltantes().divide(getTotal(), BigDecimal.ROUND_HALF_UP)
            .multiply(new BigDecimal(100)).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
   }

   public static double getIndice(BigDecimal total, BigDecimal cantidad) {
      return total.doubleValue() == 0d ? 0d : cantidad.divide(total, BigDecimal.ROUND_HALF_UP).multiply(
            new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
   }

   public BigDecimal getFaltantesNoSustituir() {
      return faltantesNoSustituir;
   }

   public void setFaltantesNoSustituir(BigDecimal faltantesNoSustituir) {
      this.faltantesNoSustituir = faltantesNoSustituir;
   }
}
