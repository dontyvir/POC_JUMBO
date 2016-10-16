/*
 * Created on 17-abr-2009
 *
 */
package cl.jumbo.common.dto;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * @author jdroguett
 *  
 */
public class Categoria {
   private Hashtable attributes = new Hashtable();
   private Hashtable data = new Hashtable();
   private List children = new ArrayList();

   public Hashtable getAttributes() {
      return attributes;
   }

   public void setAttributes(Hashtable attributes) {
      this.attributes = attributes;
   }

   public List getChildren() {
      return children;
   }

   public void setChildren(List children) {
      this.children = children;
   }

   public void addChildren(Categoria c) {
      children.add(c);
   }

   public void putId(int id) {
      attributes.put("id", id + "");
   }

   public void putAttributes(String attr, String val) {
      attributes.put(attr, val);
   }

   public Hashtable getData() {
      return data;
   }

   public void setData(Hashtable data) {
      this.data = data;
   }

   public void putData(String attr, String val) {
      this.data.put(attr, val);
   }
}
