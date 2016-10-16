/*
 * Created on 22-sep-2009
 *
 */
package cl.cencosud.informes.datos;

import java.math.BigDecimal;

/**
 * @author jdroguett
 *  
 */
public class Ranking implements Comparable {
   private Local local;
   private BigDecimal pickeados;
   private BigDecimal sustituidos;
   private BigDecimal faltantes;
   private BigDecimal faltantesNoSustituir;

   public BigDecimal getFaltantesNoSustituir() {
      return faltantesNoSustituir;
   }

   public void setFaltantesNoSustituir(BigDecimal faltantesNoSustituir) {
      this.faltantesNoSustituir = faltantesNoSustituir;
   }

   public Ranking() {
      pickeados = new BigDecimal(0);
      faltantes = new BigDecimal(0);
      sustituidos = new BigDecimal(0);
      faltantesNoSustituir = new BigDecimal(0);
   }

   public BigDecimal getFaltantes() {
      return faltantes;
   }

   public void setFaltantes(BigDecimal faltantes) {
      this.faltantes = faltantes;
   }

   public Local getLocal() {
      return local;
   }

   public void setLocal(Local local) {
      this.local = local;
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

   public void addPickeados(BigDecimal b) {
      this.pickeados = this.pickeados.add(b);
   }

   public void addFaltantes(BigDecimal b) {
      this.faltantes = this.faltantes.add(b);
   }

   public void addSustituidos(BigDecimal b) {
      this.sustituidos = this.sustituidos.add(b);
   }

   public void addFaltantesNoSustituir(BigDecimal b) {
      this.faltantesNoSustituir = this.faltantesNoSustituir.add(b);
   }

   public BigDecimal getTotalFaltantes() {
      return faltantes.add(sustituidos);
   }

   public double getIndiceSustitucion() {
      return getTotalFaltantes().doubleValue() == 0d ? 0d : sustituidos.divide(getTotalFaltantes(),
            BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
   }

   public double getIndiceFaltantes() {
      return getPickeados().doubleValue() == 0d ? 0d : getTotalFaltantes().divide(getPickeados(),
            BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
   }

   public int compareTo(Object r) {
      return (int) (100*(this.getIndiceFaltantes() - ((Ranking) r).getIndiceFaltantes()));
   }
}
