package cl.bbr.boc.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;

public class ClienteDTO implements Serializable{
	
	/**
	 * 
	 */
	private static SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");	                          
	private static SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
	
	private static final long serialVersionUID = 1L;
	private int id ;	
	private String party_id;
	private int rut ;
	private String rutDv;
	private String nombre;
	private String apellidoPaterno;	
	private String genero;
	private Date fechaNacimiento;
	private String pais;
	private String region;
	private String comuna;
	private String ciudad;
	private String calle;
	private String numero;
	private String departamento;
	private String fonoHogar;
	private String fonoMovil;
	private String email;
	private float totalUnidades;
	private float totalSku;
	private String fonoCodigoUno;
	private String fonoCodigoDos;	
	//private List listaProductos;
		
	private float totalUnidadesClientes;	
	private float totalSkuClientes;
	
	private int totalProductos;	
	private int totalProductosAdd;
	private int totalProductosNoAdd;
	private List listaProductoNoAdd;
	private List listaProducto;
	

	/**
	 *  Codigo identificador de la compra histórica.
	 * */
	private int chID;
		
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getParty_id() {
		return party_id;
	}
	public void setParty_id(String party_id) {
		this.party_id = party_id;
	}
	public int getRut() {
		return rut;
	}
	public void setRut(int rut) {
		this.rut = rut;
	}
	public String getRutDv() {
		return rutDv;
	}
	public void setRutDv(String rutDv) {
		this.rutDv = rutDv;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellidoPaterno() {
		return apellidoPaterno;
	}
	public void setApellidoPaterno(String apellidoPaterno) {
		this.apellidoPaterno = apellidoPaterno;
	}	
	public String getGenero() {
		return genero;
	}
	public void setGenero(String genero) {
		this.genero = genero;
	}
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}
	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	public String getPais() {
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getComuna() {
		return comuna;
	}
	public void setComuna(String comuna) {
		this.comuna = comuna;
	}
	public String getCiudad() {
		return ciudad;
	}
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
	public String getCalle() {
		return calle;
	}
	public void setCalle(String calle) {
		this.calle = calle;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getDepartamento() {
		return departamento;
	}
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
	public String getFonoHogar() {
		return fonoHogar;
	}
	public void setFonoHogar(String fonoHogar) {
		this.fonoHogar = fonoHogar;
	}
	public String getFonoMovil() {
		return fonoMovil;
	}
	public void setFonoMovil(String fonoMovil) {
		this.fonoMovil = fonoMovil;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public float getTotalUnidades() {
		return totalUnidades;
	}
	public void setTotalUnidades(float totalUnidades) {
		this.totalUnidades = totalUnidades;
	}
	public float getTotalSku() {
		return totalSku;
	}
	public void setTotalSku(float totalSku) {
		this.totalSku = totalSku;
	}
	/*
	public List getListaProductos() {
		return listaProductos;
	}
	public void setListaProductos(List listaProductos) {
		this.listaProductos = listaProductos;
	}
	*/
	public float getTotalUnidadesClientes() {
		return totalUnidadesClientes;
	}
	public void setTotalUnidadesClientes(float totalUnidadesClientes) {
		this.totalUnidadesClientes = totalUnidadesClientes;
	}
	public float getTotalSkuClientes() {
		return totalSkuClientes;
	}
	public void setTotalSkuClientes(float totalSkuClientes) {
		this.totalSkuClientes = totalSkuClientes;
	}
	public String getFonoCodigoUno() {
		return fonoCodigoUno;
	}
	public void setFonoCodigoUno(String fonoCodigoUno) {
		this.fonoCodigoUno = fonoCodigoUno;
	}
	public String getFonoCodigoDos() {
		return fonoCodigoDos;
	}
	public void setFonoCodigoDos(String fonoCodigoDos) {
		this.fonoCodigoDos = fonoCodigoDos;
	}
	/**
	 *  Obtiene el codigo identificador de la compra historica.	 
	 * */
	public int getChID() {
		return chID;
	}
	public void setChID(int chID) {
		this.chID = chID;
	}
	
	public int getTotalProductos() {
		return totalProductos;
	}
	public void setTotalProductos(int totalProductos) {
		this.totalProductos = totalProductos;
	}
	public int getTotalProductosAdd() {
		return totalProductosAdd;
	}
	public void setTotalProductosAdd(int totalProductosAdd) {
		this.totalProductosAdd = totalProductosAdd;
	}
	public int getTotalProductosNoAdd() {
		return totalProductosNoAdd;
	}
	public void setTotalProductosNoAdd(int totalProductosNoAdd) {
		this.totalProductosNoAdd = totalProductosNoAdd;
	}
	public List getListaProductoNoAdd() {
		return listaProductoNoAdd;
	}
	public void setListaProductoNoAdd(List listaProductoNoAdd) {
		this.listaProductoNoAdd = listaProductoNoAdd;
	}
	public List getListaProducto() {
		return listaProducto;
	}
	public void setListaProducto(List listaProducto) {
		this.listaProducto = listaProducto;
	}
	
	public ClienteDTO crearDTOCliente(ClienteDTO clienteDTO, HSSFRow rowSheet, String[] arrayCabezeras, String namerHoja) throws Exception {		
		HSSFCell cell = null;
		String headerName = "";
		String valorString = "";
		int valorInt = 0;	
		float valorFloat = 0;	
		
		try{
			headerName = arrayCabezeras[0];//"PARTY_ID";
			cell = rowSheet.getCell(0);
			//clienteDTO.setParty_id("");
			if(null!=cell){
				valorString = ClienteDTO.getValorCelda(cell);
				if(valorString.equals("") || valorString.length()==0){
					throw new Exception("");	
				}else{
					clienteDTO.setParty_id( ClienteDTO.getValorCelda(cell));	
				}	
			}

			headerName = arrayCabezeras[1];//"RUT";
			cell = rowSheet.getCell(1);
			//clienteDTO.setRut(0);
			if(null!=cell){				
				//clienteDTO.setRut( Integer.parseInt( getValorCelda(cell)) );
				valorInt = Integer.parseInt( getValorCelda(cell));
				if(valorInt==0){
					throw new Exception("");	
				}else{
					clienteDTO.setRut( Integer.parseInt( getValorCelda(cell)) );
				}
			}

			headerName = arrayCabezeras[2];//"DV";
			cell = rowSheet.getCell(2);
			//clienteDTO.setRutDv("");
			if(null!=cell){
				//clienteDTO.setRutDv( getValorCelda(cell) );
				valorString = ClienteDTO.getValorCelda(cell);
				if(valorString.equals("") || valorString.length()==0){
					throw new Exception("");	
				}else{
					clienteDTO.setRutDv( ClienteDTO.getValorCelda(cell));	
				}
			}

			headerName = arrayCabezeras[3];//"NOMBRE";
			cell = rowSheet.getCell(3);
			//clienteDTO.setNombre("");
			if(null!=cell){
				//clienteDTO.setNombre( getValorCelda(cell) );
				valorString = ClienteDTO.getValorCelda(cell);
				if(valorString.equals("") || valorString.length()==0){
					throw new Exception("");	
				}else{
					clienteDTO.setNombre( ClienteDTO.getValorCelda(cell));	
				}
			}

			headerName = arrayCabezeras[4];//"APELLIDO_PATERNO";
			cell = rowSheet.getCell(4);
			//clienteDTO.setApellidoPaterno("");
			if(null!=cell){
				//clienteDTO.setApellidoPaterno( getValorCelda(cell) );
				valorString = ClienteDTO.getValorCelda(cell);
				if(valorString.equals("") || valorString.length()==0){
					throw new Exception("");	
				}else{
					clienteDTO.setApellidoPaterno( ClienteDTO.getValorCelda(cell));	
				}
			}

			headerName = arrayCabezeras[5];//"GENERO";
			cell = rowSheet.getCell(5);
			//clienteDTO.setGenero("");			
			if(null!=cell){
				//clienteDTO.setGenero( getValorCelda(cell) );
				valorString = ClienteDTO.getValorCelda(cell).toLowerCase();
				if(valorString.equals("f") || valorString.equals("m")){
					if(valorString.equals("") || valorString.length()==0){
						throw new Exception("");	
					}else{
						clienteDTO.setGenero( ClienteDTO.getValorCelda(cell));	
					}	
				}else{
					throw new Exception("Decripción: Genero (M) ó (F).");
				}
				
			}

			headerName = arrayCabezeras[6];//"FEC_NAC";
			cell = rowSheet.getCell(6);								
			//clienteDTO.setFechaNacimiento(null);
			if(null!=cell){				
				Date date = getValorCeldaDate(cell);
				if(null!=date){
					clienteDTO.setFechaNacimiento( date );
				}else{
					throw new Exception("");
				}
			}

			headerName = arrayCabezeras[7];//"PAIS";
			cell = rowSheet.getCell(7);
			//clienteDTO.setPais("");
			if(null!=cell){
				//clienteDTO.setPais( getValorCelda(cell) );
				valorString = ClienteDTO.getValorCelda(cell);
				if(valorString.equals("") || valorString.length()==0){
					throw new Exception("");	
				}else{
					clienteDTO.setPais( ClienteDTO.getValorCelda(cell));	
				}
			}

			headerName = arrayCabezeras[8];//"REGION";
			cell = rowSheet.getCell(8);
			//clienteDTO.setRegion("");
			if(null!=cell){
				//clienteDTO.setRegion( getValorCelda(cell) );
				valorString = ClienteDTO.getValorCelda(cell);
				if(valorString.equals("") || valorString.length()==0){
					throw new Exception("");	
				}else{
					clienteDTO.setRegion( ClienteDTO.getValorCelda(cell));	
				}
			}

			headerName = arrayCabezeras[9];//"COMUNA";
			cell = rowSheet.getCell(9);
			//clienteDTO.setComuna("");
			if(null!=cell){
				//clienteDTO.setComuna( getValorCelda(cell) );
				valorString = ClienteDTO.getValorCelda(cell);
				if(valorString.equals("") || valorString.length()==0){
					throw new Exception("");	
				}else{
					clienteDTO.setComuna( ClienteDTO.getValorCelda(cell));	
				}
			}

			headerName = arrayCabezeras[10];//"CIUDAD";
			cell = rowSheet.getCell(10);
			//clienteDTO.setCiudad("");
			if(null!=cell){
				//clienteDTO.setCiudad( getValorCelda(cell) );
				valorString = ClienteDTO.getValorCelda(cell);
				if(valorString.equals("") || valorString.length()==0){
					throw new Exception("");	
				}else{
					clienteDTO.setCiudad( ClienteDTO.getValorCelda(cell));	
				}
			}

			headerName = arrayCabezeras[11];//"CALLE";
			cell = rowSheet.getCell(11);
			//clienteDTO.setCalle("");
			if(null!=cell){
				//clienteDTO.setCalle( getValorCelda(cell) );
				valorString = ClienteDTO.getValorCelda(cell);
				if(valorString.equals("") || valorString.length()==0){
					throw new Exception("");	
				}else{
					clienteDTO.setCalle( ClienteDTO.getValorCelda(cell));	
				}
			}

			headerName = arrayCabezeras[12];//"FONO_COD_NUMERO";
			cell = rowSheet.getCell(12);
			clienteDTO.setFonoCodigoUno("");
			if(null!=cell){							
				//if(valorString.equals("") || valorString.length()==0){
				//	throw new Exception("");	
				//}else{
					clienteDTO.setFonoCodigoUno( ClienteDTO.getValorCelda(cell));	
					//}
				/*				
				if(getValorCelda(cell).equals("") || getValorCelda(cell).length()==0){
					clienteDTO.setFonoCodigoUno( "2" );
				}else{
					clienteDTO.setFonoCodigoUno( getValorCelda(cell) );
				}
				*/				
			}			
			
			headerName = arrayCabezeras[13];//"NUMERO";
			cell = rowSheet.getCell(13);
			clienteDTO.setNumero("");
			if(null!=cell){								
				//if(valorString.equals("") || valorString.length()<=7){
				//	throw new Exception("largo de 8 caracteres.");	
				//}else{
					clienteDTO.setNumero( ClienteDTO.getValorCelda(cell));	
					//}
				
				/*
				clienteDTO.setNumero( getValorCelda(cell) );				
				if(clienteDTO.getNumero().length()<=7){
					clienteDTO.setNumero( "12345678" );
				}
				*/
			}

			headerName = arrayCabezeras[14];//"DEPTO";
			cell = rowSheet.getCell(14);
			clienteDTO.setDepartamento("");
			if(null!=cell){
				//clienteDTO.setDepartamento( getValorCelda(cell) );
				valorString = ClienteDTO.getValorCelda(cell);
				if(valorString.equals("") || valorString.length()==0){
					throw new Exception("");	
				}else{
					clienteDTO.setDepartamento( ClienteDTO.getValorCelda(cell));	
				}
			}

			headerName = arrayCabezeras[15];//"FONO_COD_MOVIL";
			cell = rowSheet.getCell(15);
			clienteDTO.setFonoCodigoDos("");
			if(null!=cell){				
				//if(valorString.equals("") || valorString.length()==0){
				//	throw new Exception("");	
					//}else{
					clienteDTO.setFonoCodigoDos( ClienteDTO.getValorCelda(cell));	
					//}
				/*
				if(getValorCelda(cell).equals("") || getValorCelda(cell).length()==0){
					clienteDTO.setFonoCodigoDos( "2" );
				}else{
					clienteDTO.setFonoCodigoDos( getValorCelda(cell) );
				}
				*/				
			}
			
			headerName = arrayCabezeras[16];//"FONO_HOGAR";
			cell = rowSheet.getCell(16);
			clienteDTO.setFonoHogar("");
			if(null!=cell){				
				//if(valorString.equals("") || valorString.length()<=7){
					//throw new Exception("largo de 8 caracteres.");	
				//}else{
					clienteDTO.setFonoCodigoDos( ClienteDTO.getValorCelda(cell));	
					//}
				/*
				clienteDTO.setFonoHogar( getValorCelda(cell) );
				if(clienteDTO.getFonoHogar().length()<=7){
					clienteDTO.setFonoHogar( "12345678" );
				}
				*/
			}

			headerName = arrayCabezeras[17];//"FONO_MOVIL";
			cell = rowSheet.getCell(17);
			clienteDTO.setFonoMovil("");
			if(null!=cell){
				clienteDTO.setFonoMovil( getValorCelda(cell) );
				//if(clienteDTO.getFonoMovil().length()<=7){
				//	clienteDTO.setFonoMovil("12345678");
				//}
			}

			headerName = arrayCabezeras[18];//"EMAIL";
			cell = rowSheet.getCell(18);
			//clienteDTO.setEmail("");
			if(null!=cell){
				//clienteDTO.setEmail( getValorCelda(cell) );
				valorString = ClienteDTO.getValorCelda(cell);
				if(valorString.equals("") || valorString.length()==0){
					throw new Exception("");	
				}else{
					clienteDTO.setEmail( ClienteDTO.getValorCelda(cell));	
				}
			}

			headerName = arrayCabezeras[19];//"UNIDADES";
			cell = rowSheet.getCell(19);
			clienteDTO.setTotalUnidades(0);
			if(null!=cell){
				//clienteDTO.setTotalUnidades( getValorCeldaInt(cell) );
				valorFloat = ClienteDTO.getValorCeldaInt(cell);
				if(valorFloat<0){
					throw new Exception("");	
				}else{
					clienteDTO.setTotalUnidades( valorFloat );	
				}
			}

			headerName = arrayCabezeras[20];//"SKUS_TOTAL";
			cell = rowSheet.getCell(20);
			clienteDTO.setTotalSku(0);
			if(null!=cell){
				//clienteDTO.setTotalSku( Integer.parseInt( getValorCelda(cell)) );
				valorInt = Integer.parseInt( getValorCelda(cell));
				if(valorInt<0){
					throw new Exception("");	
				}else{
					clienteDTO.setTotalSku( valorInt );	
				}
			}			
			
		}catch(Exception e){
			throw new Exception ("Error al leer Celda en Hoja ["+namerHoja+"]: " +					
					"Columna["+headerName+"] " +
					", Fila["+(cell.getRowIndex()+1)+"]. " + e.getMessage());
		}
		return clienteDTO;
	}
	
	
	public static String getValorCelda(HSSFCell cell) throws Exception {	
		String valueCell = "";						

		if(cell.getCellType() == HSSFCell.CELL_TYPE_BLANK){
			return "";
		}
		if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
			valueCell = String.valueOf((int)cell.getNumericCellValue()).trim();					
			return valueCell.toString();
		}
		if(cell.getCellType() == HSSFCell.CELL_TYPE_STRING){
			valueCell= cell.getRichStringCellValue().getString().trim();
			return valueCell;
		}	
		return "";
	}
	
	public static Date getValorCeldaDate(HSSFCell cell) throws Exception {
		Date valueCell = null;
		Date date = null;
		if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
			valueCell = cell.getDateCellValue();			
			date = format2.parse( format2.format( format1.parse( format1.format(valueCell) )) );			
		}
		return date;
	}
	
	public static float getValorCeldaInt(HSSFCell cell) throws Exception {		
		return (float)cell.getNumericCellValue();				
	}
}
