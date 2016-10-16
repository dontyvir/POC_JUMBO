package cl.bbr.boc.dto;

import java.io.Serializable;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;

public class ProductoDTO implements Serializable {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String party_id;
	private long ean;
	private String sku;
	private String itemDescripcion;
	private float unidades;
	private float skusTotal;
	private int idCliente;
	
	public String getParty_id() {
		return party_id;
	}
	public void setParty_id(String party_id) {
		this.party_id = party_id;
	}
	public long getEan() {
		return ean;
	}
	public void setEan(long ean) {
		this.ean = ean;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getItemDescripcion() {
		return itemDescripcion;
	}
	public void setItemDescripcion(String itemDescripcion) {
		this.itemDescripcion = itemDescripcion;
	}
	public float getUnidades() {
		return unidades;
	}
	public void setUnidades(float unidades) {
		this.unidades = unidades;
	}
	public float getSkusTotal() {
		return skusTotal;
	}
	public void setSkusTotal(float skusTotal) {
		this.skusTotal = skusTotal;
	}
	public int getIdCliente() {
		return idCliente;
	}
	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}
	
	public ProductoDTO crearObjetoClientesSku(ProductoDTO productoDTO, HSSFRow rowSheet, String[] arrayCabezeras,  String namerHoja) throws Exception {	
		HSSFCell cell = null;
		String headerName = "";
		String valorString = "";
		long valorLong = 0;		

		try{

			headerName = arrayCabezeras[0]; //"PARTY_ID";
			cell = rowSheet.getCell(0);
			//productoDTO.setParty_id("");
			if(null!=cell){
				valorString = ClienteDTO.getValorCelda(cell);
				if(valorString.equals("") || valorString.length()==0){
					throw new Exception("");	
				}else{
					productoDTO.setParty_id( ClienteDTO.getValorCelda(cell));	
				}				
			}

			headerName =  arrayCabezeras[1]; //"EAN";
			cell = rowSheet.getCell(1);
			//productoDTO.setEan(0);
			if(null!=cell){
				valorLong = getValorCeldaLong(cell);
				if(valorLong==0){
					throw new Exception("");	
				}else{
					productoDTO.setEan( getValorCeldaLong(cell));	
				}											
			}
 
			headerName =  arrayCabezeras[2]; //"SKU";
			cell = rowSheet.getCell(2);
			//productoDTO.setSku("");
			if(null!=cell){
				valorString = ClienteDTO.getValorCelda(cell);
				if(valorString.equals("") || valorString.length()==0){
					throw new Exception("");	
				}else{
					productoDTO.setSku( ClienteDTO.getValorCelda(cell));	
				}
			}

			headerName = arrayCabezeras[3]; //"ITEM_DESC";
			cell = rowSheet.getCell(3);
			//productoDTO.setItemDescripcion("");
			if(null!=cell){
				valorString = ClienteDTO.getValorCelda(cell);
				if(valorString.equals("") || valorString.length()==0){
					throw new Exception("");
				}else{
					productoDTO.setItemDescripcion( ClienteDTO.getValorCelda(cell));	
				}
			}

			headerName =  arrayCabezeras[4]; //"UNIDADES";
			cell = rowSheet.getCell(4);
			productoDTO.setUnidades(0);
			if(null!=cell){
				productoDTO.setUnidades( ClienteDTO.getValorCeldaInt(cell));				
			}

			headerName =  arrayCabezeras[5]; //"SKUS_TOTAL";
			cell = rowSheet.getCell(5);
			productoDTO.setSkusTotal(0);
			if(null!=cell){
				productoDTO.setSkusTotal( ClienteDTO.getValorCeldaInt(cell));				
			}
			
		}catch(Exception e){
			throw new Exception ("Error al leer Celda en Hoja ["+namerHoja+"]: " +
					"Columna["+headerName+"] " +
					", Fila["+(cell.getRowIndex()+1)+"]. " + e.getMessage());
		}
		return productoDTO;
	}
	
	public static long getValorCeldaLong(HSSFCell cell) throws Exception {		
		return (long)cell.getNumericCellValue();				
	}
	
}
