/*
 * Creado el 07-nov-2012
 *
 * TODO Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
package cl.bbr.bol.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import cl.bbr.bol.dao.FaltantesDAO;
import cl.bbr.bol.dto.CabeceraInformeFaltantesDTO;
import cl.bbr.bol.dto.ParametroConsultaFaltantesDTO;
import cl.bbr.bol.dto.ProductoFaltantesDTO;
import cl.bbr.jumbocl.shared.conexion.ConexionUtil;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.jumbo.common.dao.jdbc.JdbcDAO;

/**
 * @author Sebastian
 *
 * TODO Para cambiar la plantilla de este comentario generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
public class FaltantesJdbc extends JdbcDAO implements FaltantesDAO{

	private static ConexionUtil conexionUtil = new ConexionUtil();
	
	public HashMap getDatosCabecera(int validacion, String fechaConsulta, long jornadaActual, long idLocal) throws DAOException {

		/*Datos que siempre vienen
		 * validacion
		 * idLocal
		 * */
		/*QueryString*/
		String sqlCabecera = "";
		String sqlListadoHorarios = "";
		/*Fin QueryString*/
		if (fechaConsulta.compareToIgnoreCase("") == 0){
			fechaConsulta = "SELECT CURRENT_DATE FROM SYSIBM.SYSDUMMY1";
		}else {
			fechaConsulta = "'" + fechaConsulta + "'";
		}
		
		/*obtener datos cabecera*/
		if (jornadaActual == 0){
			sqlCabecera = "SELECT ID_JPICKING, FECHA, ID_HOR_PICK FROM BO_JORNADAS_PICK " +
						  "WHERE ID_LOCAL = " + idLocal + " AND ID_SEMANA = (" +
						  "SELECT ID_SEMANA FROM BO_SEMANAS WHERE (" + fechaConsulta + ") BETWEEN FINI AND FFIN) " +
						  "AND FECHA = (" + fechaConsulta + ") AND ID_HOR_PICK = ( " +
						  "SELECT ID_HOR_PICK FROM BO_HORARIO_PICK WHERE (SELECT CURRENT_TIME FROM SYSIBM.SYSDUMMY1) BETWEEN HINI AND HFIN " +
						  "AND ID_LOCAL = " + idLocal + " AND ID_SEMANA = (SELECT ID_SEMANA FROM BO_SEMANAS " +
						  "WHERE (" + fechaConsulta + ") BETWEEN FINI AND FFIN))" ;
		}else {
			sqlCabecera = "SELECT ID_JPICKING, FECHA, ID_HOR_PICK FROM BO_JORNADAS_PICK WHERE ID_JPICKING = " + jornadaActual;
		}
		/*FIN OBTENER DATOS CABECERA*/
		
		/*OBTENER LISTADO HORARIOS POR LOCAL*/
		sqlListadoHorarios = "SELECT ID_HOR_PICK, HINI, HFIN FROM BO_HORARIO_PICK WHERE BO_HORARIO_PICK.ID_LOCAL = " + idLocal + " " +
							 "AND ID_SEMANA = (SELECT ID_SEMANA FROM BO_SEMANAS WHERE (" + fechaConsulta + ") BETWEEN FINI AND FFIN)";	
		/*FIN OBTENER LISTADO HORARIOS POR LOCAL*/
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		HashMap mapaCabecera = new HashMap();
		List horariosLocal = new ArrayList();
		try {
			/*OBTENER CABECERA*/
			con = conexionUtil.getConexion();
			ps = con.prepareStatement(sqlCabecera + " WITH UR");
			rs = ps.executeQuery();
			while (rs.next()) {
				
				mapaCabecera.put("jornadaConsulta", new Long(rs.getLong("ID_JPICKING")));
				mapaCabecera.put("fechaConsulta", rs.getString("FECHA"));
				mapaCabecera.put("horarioConsulta", new Long(rs.getLong("ID_HOR_PICK")));
			}
			/*OBTENER LISTADO*/
			ps = con.prepareStatement(sqlListadoHorarios + " WITH UR");
			rs = ps.executeQuery();
			while (rs.next()) {
				HashMap horario = new HashMap();
				horario.put("idHorario", new Long(rs.getLong("ID_HOR_PICK")));
				horario.put("horarioLocal",(String)rs.getString("HINI").substring(0,5) + "-"+ (String)rs.getString("HFIN").substring(0,5));
				horariosLocal.add(horario);
			}
			
			mapaCabecera.put("horariosLocal", horariosLocal);
			
         }catch(Exception e){
         	e.printStackTrace();
         	throw new DAOException(e);
         }finally {
         	close(rs, ps, con);
         }
         return mapaCabecera;
	}

	public HashMap getInformeFaltantes(ParametroConsultaFaltantesDTO parametro) throws DAOException{
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		CabeceraInformeFaltantesDTO cabecera = new CabeceraInformeFaltantesDTO();
		List resultadoEstadistica = new ArrayList();
		HashMap resultado = new HashMap();
		/* Variables de Conexion */
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		/* Obtencion QueryString */
//		ResourceBundle rb = ResourceBundle.getBundle("consultasFaltantesBOL");
//		String strSqlJornadasAnteriores = rb.getString("jornadasAnteriores");
		String strSqlJornadasAnteriores = "SELECT ID_JPICKING FROM (SELECT * FROM BO_JORNADAS_PICK WHERE ID_LOCAL = idLocal AND FECHA = " +
										  "(fechaConsultaAnterior) ORDER BY ID_HOR_PICK DESC)Y UNION ALL SELECT ID_JPICKING FROM (" +
										  "SELECT * FROM BO_JORNADAS_PICK WHERE ID_LOCAL = idLocal AND FECHA = (fechaConsultaActual) " +
										  "AND ID_HOR_PICK <= (horarioConsulta) ORDER BY ID_HOR_PICK DESC)X FETCH FIRST 3 ROWS ONLY";
//		String strSqlJornadasPosteriores = rb.getString("jornadasPosteriores");
//INDRA 10-01-2013
		String strSqlJornadasPosteriores = //"select N.ID_JPICKING, COUNT(ID_PEDIDO) CANT_PEDIDOS FROM BO_PEDIDOS RIGHT OUTER JOIN (" +
//INDRA 10-01-2013
										   "SELECT ID_JPICKING FROM(SELECT ID_JPICKING FROM BO_JORNADAS_PICK WHERE ID_HOR_PICK IN (" +
										   "SELECT * FROM (SELECT ID_HOR_PICK FROM BO_HORARIO_PICK WHERE ID_SEMANA IN (" +
										   "SELECT ID_SEMANA FROM BO_SEMANAS WHERE fechaConsultaPosterior BETWEEN FINI AND FFIN) " +
										   "and id_local = idLocal UNION ALL SELECT ID_HOR_PICK FROM BO_HORARIO_PICK WHERE ID_SEMANA IN (" +
										   "SELECT ID_SEMANA FROM BO_SEMANAS WHERE fechaConsultaActual BETWEEN FINI AND FFIN) and " +
										   "id_local = idLocal AND ID_HOR_PICK >= horarioConsulta)Z FETCH FIRST 4 ROWS ONLY) AND " +
										   "ID_LOCAL = idLocal AND FECHA BETWEEN (fechaConsultaActual) AND (fechaConsultaPosterior) ORDER BY FECHA ASC" +
										   ") x where ID_JPICKING NOT IN (" +
										   "SELECT ID_JPICKING FROM BO_JORNADAS_PICK WHERE ID_HOR_PICK IN (" +
										   "SELECT ID_HOR_PICK FROM BO_HORARIO_PICK WHERE ID_SEMANA = ( " +
										   "SELECT ID_SEMANA FROM BO_SEMANAS WHERE (fechaConsultaActual) BETWEEN FINI AND FFIN) AND " +
										   "ID_HOR_PICK <  horarioConsulta AND ID_LOCAL = idLocal) AND ID_LOCAL = idLocal AND " +
//INDRA 10-01-2013
										   "FECHA = (fechaConsultaActual)) FETCH FIRST 4 ROWS ONLY " ;
											//") N ON (BO_PEDIDOS.ID_JPICKING = N.ID_JPICKING) GROUP BY N.ID_JPICKING";
//INDRA 10-01-2013											
//		String strSqlProductosFaltantesAnteriores = rb.getString("prodFaltantesAnteriores");
		String strSqlProductosFaltantesAnteriores = "SELECT DISTINCT BO_DETALLE_PEDIDO.ID_PRODUCTO FROM BO_PEDIDOS " +
													"INNER JOIN BO_DETALLE_PEDIDO ON BO_PEDIDOS.ID_PEDIDO = BO_DETALLE_PEDIDO.ID_PEDIDO " +
													"WHERE BO_PEDIDOS.ID_JPICKING IN (jornadasAnteriores) AND " +
													"BO_DETALLE_PEDIDO.CANT_FALTAN > 0 UNION " +
													"Select ID_PRODUCTO FROM BO_DETALLE_PEDIDO WHERE ID_DETALLE IN (" +
													"SELECT DISTINCT BO_DETALLE_PICKING.ID_DETALLE FROM BO_PEDIDOS " +
													"INNER JOIN BO_DETALLE_PICKING ON BO_PEDIDOS.ID_PEDIDO = BO_DETALLE_PICKING.ID_PEDIDO " +
													"WHERE BO_PEDIDOS.ID_JPICKING IN (jornadasAnteriores) AND BO_DETALLE_PICKING.SUSTITUTO = 'S')";
//		String strSqlProdJornadasActuales = rb.getString("prodJornadasActuales");
		String strSqlProdJornadasActuales = "SELECT PRODUCTOS_ANTERIORES.ID_JPICKING ID_JPICKING, PRODUCTOS_ANTERIORES.ID_PRODUCTO ID_PRODUCTO, " +
											"PRODUCTOS_ANTERIORES.DESCRIPCION, PRODUCTOS_ANTERIORES.NOMBRE,PRODUCTOS_ANTERIORES.CANT_SOLIC, " +
											"PRODUCTOS_ANTERIORES.CANT_PROD_EN_OP_X_JORNADA CANT_PROD_EN_OP_X_JORNADA, PRODUCTOS_ANTERIORES.TOTAL_OPS_X_JORNADA TOTAL_OPS_X_JORNADA," +
											"ROUND(((DOUBLE(PRODUCTOS_ANTERIORES.CANT_PROD_EN_OP_X_JORNADA) / DOUBLE(PRODUCTOS_ANTERIORES.TOTAL_OPS_X_JORNADA)) * 100),2) PORC_PRESENCIA " +
											"FROM (SELECT BO_DETALLE_PEDIDO.ID_PRODUCTO ID_PRODUCTO,SUM(BO_DETALLE_PEDIDO.CANT_SOLIC) CANT_SOLIC," +
											"BO_DETALLE_PEDIDO.DESCRIPCION,BO_SECTOR.NOMBRE,COUNT(BO_PEDIDOS.ID_PEDIDO) CANT_PROD_EN_OP_X_JORNADA," +
											"X.ID_JPICKING,X.OPS_X_JORNADA TOTAL_OPS_X_JORNADA FROM BO_PEDIDOS " +
											"INNER JOIN BO_DETALLE_PEDIDO ON BO_DETALLE_PEDIDO.ID_PEDIDO = BO_PEDIDOS.ID_PEDIDO " +
											"INNER JOIN BO_SECTOR ON BO_DETALLE_PEDIDO.ID_SECTOR = BO_SECTOR.ID_SECTOR " +
											"INNER JOIN (SELECT COUNT(DISTINCT(BO_PEDIDOS.ID_PEDIDO)) OPS_X_JORNADA, " +
											"BO_PEDIDOS.ID_JPICKING FROM BO_JORNADAS_PICK, BO_PEDIDOS, BO_DETALLE_PEDIDO WHERE BO_PEDIDOS.ID_JPICKING = BO_JORNADAS_PICK.ID_JPICKING AND " +
											"BO_PEDIDOS.ID_PEDIDO = BO_DETALLE_PEDIDO.ID_PEDIDO AND BO_JORNADAS_PICK.ID_JPICKING IN (jornadasPosteriores) " +
											"GROUP BY BO_PEDIDOS.ID_JPICKING ORDER BY BO_PEDIDOS.ID_JPICKING) X ON BO_PEDIDOS.ID_JPICKING = X.ID_JPICKING " +
											"GROUP BY BO_DETALLE_PEDIDO.ID_PRODUCTO, BO_DETALLE_PEDIDO.DESCRIPCION,BO_SECTOR.NOMBRE, X.ID_JPICKING, X.OPS_X_JORNADA) PRODUCTOS_ANTERIORES " +
											"WHERE PRODUCTOS_ANTERIORES.ID_PRODUCTO IN (productosFaltantesAnteriores)";
//		String strSqlJornadaConsulta = rb.getString("jornadaConsulta");
		String strSqlJornadaConsulta = "SELECT id_hor_pick, fecha, ID_LOCAL FROM BO_JORNADAS_PICK WHERE id_jpicking = jornadaActual";
		String fechaConsultaAnterior = "";
		String fechaConsultaActual = "";
		String fechaConsultaPosterior = "";
		String textoHorario = "";
		
		/*Fin Obtencion QueryString*/
		String horarioConsulta = "";
		if (parametro != null){
			if (parametro.getNumeroJornada() > 0){
				try{
					strSqlJornadaConsulta = strSqlJornadaConsulta.replaceAll("jornadaActual", String.valueOf(parametro.getNumeroJornada()));
					con = conexionUtil.getConexion();
					ps = con.prepareStatement(strSqlJornadaConsulta + " WITH UR");
					rs = ps.executeQuery();
					long idLocal = 0;
					while (rs.next()) {
						idLocal = rs.getLong("ID_LOCAL");
						horarioConsulta = rs.getString("ID_HOR_PICK");
						fechaConsultaAnterior = "date('"+ rs.getString("FECHA") + "') - 1 day";
						fechaConsultaActual = "date('" + rs.getString("FECHA") + "')";
						fechaConsultaPosterior = "date('" + rs.getString("FECHA") + "') + 2 day";
					}
					if (idLocal != parametro.getIdLocal()){
						HashMap retornoError = new HashMap();
						retornoError.put("error", "Error. El ID de la Jornada no corresponde al Local");
						return retornoError;
					}
					String  strSqlTextoHorario = "Select hini, hfin from BO_HORARIO_PICK where id_hor_pick = " + horarioConsulta;
					con = conexionUtil.getConexion();
					ps = con.prepareStatement(strSqlTextoHorario + " WITH UR");
					rs = ps.executeQuery();
					while (rs.next()) {
						textoHorario = rs.getString("HINI").substring(0, 5) + "-" + rs.getString("HFIN").substring(0,5);
					}
				}catch (Exception e){
					throw new DAOException(e);
				}
			} else {
				try {
					String[] txtHorario = parametro.getTextoJornada().split("-");
					String consultaHorarioActual  = "select id_hor_pick from BO_HORARIO_PICK " +
													"where id_local = " + parametro.getIdLocal() + " " +  
													"and hini = '" + txtHorario[0] + "' || ':00'" + " " +
													"and hfin = '" + txtHorario[1] + "' || ':00'" + " " +
													"and id_semana = (select id_semana from bo_semanas where date('" + sdf.format(parametro.getFechaConsulta()) + "') between fini and ffin) ";
					con = conexionUtil.getConexion();
					ps = con.prepareStatement(consultaHorarioActual + " WITH UR");
					rs = ps.executeQuery();
					while (rs.next()) {
						horarioConsulta = rs.getString("id_hor_pick");
					}
					
//					horarioConsulta = new Long(parametro.getIndicadorJornada()).toString();
					if (parametro.getFechaConsulta() != null) {
						fechaConsultaAnterior = "date('"+ sdf.format(parametro.getFechaConsulta()) + "') - 1 day";
						fechaConsultaActual = "date('" + sdf.format(parametro.getFechaConsulta()) + "')";
						fechaConsultaPosterior = "date('" + sdf.format(parametro.getFechaConsulta()) + "') + 2 day";
					}
				}catch(Exception e){
					throw new DAOException(e);
				}
				
			}
		}
		
		/*Reemplazo en queryString JORNADAS*/
		strSqlJornadasAnteriores = strSqlJornadasAnteriores.replaceAll("fechaConsultaAnterior", fechaConsultaAnterior);
		strSqlJornadasAnteriores = strSqlJornadasAnteriores.replaceAll("fechaConsultaActual", fechaConsultaActual);
		strSqlJornadasAnteriores = strSqlJornadasAnteriores.replaceAll("idLocal", new Long(parametro.getIdLocal()).toString());
		strSqlJornadasAnteriores = strSqlJornadasAnteriores.replaceAll("horarioConsulta", horarioConsulta);
		
		strSqlJornadasPosteriores = strSqlJornadasPosteriores.replaceAll("fechaConsultaPosterior", fechaConsultaPosterior);
		strSqlJornadasPosteriores = strSqlJornadasPosteriores.replaceAll("fechaConsultaActual", fechaConsultaActual);
		strSqlJornadasPosteriores = strSqlJornadasPosteriores.replaceAll("idLocal", new Long(parametro.getIdLocal()).toString());
		strSqlJornadasPosteriores = strSqlJornadasPosteriores.replaceAll("horarioConsulta", horarioConsulta);
		/*Fin Reemplazo*/
		
		List listaFinal = new ArrayList();
		try {
			
			/* cadenas de Resultado */
			String jornadasAnteriores = "";
			String jornadasPosteriores = "";
			String productosFaltantesAnteriores = "";
			HashMap jornadasPedidos = new HashMap();
			
			con = conexionUtil.getConexion();
			/* Obtencion jornadas Anteriores */
			//strSqlJornadasAnteriores = strSqlJornadasAnteriores.replaceAll(String.valueOf("idLocal"), String.valueOf(parametro.getIdLocal()));
			ps = con.prepareStatement(strSqlJornadasAnteriores + " WITH UR");
			System.out.println("Jornadas Anteriores : ");
			System.out.println(strSqlJornadasAnteriores);
			rs = ps.executeQuery();
			while (rs.next()) {
				jornadasAnteriores = jornadasAnteriores + rs.getString("ID_JPICKING") + ",";
			}
			jornadasAnteriores = jornadasAnteriores.substring(0, jornadasAnteriores.length() - 1);

			/* Obtencion jornadas Posteriores */
			strSqlJornadasPosteriores = strSqlJornadasPosteriores.replaceAll(String.valueOf("idLocal"), String.valueOf(parametro.getIdLocal()));
			strSqlJornadasPosteriores = strSqlJornadasPosteriores.replaceAll("horarioConsulta", horarioConsulta);
			ps = con.prepareStatement(strSqlJornadasPosteriores	+ " WITH UR");
			System.out.println("Jornadas Posteriores : ");
			System.out.println(strSqlJornadasPosteriores);
			rs = ps.executeQuery();
			while (rs.next()) {
				jornadasPosteriores = jornadasPosteriores + rs.getString("ID_JPICKING") + ",";
//INDRA 10-01-2013				
				//jornadasPedidos.put(rs.getString("ID_JPICKING"), rs.getString("CANT_PEDIDOS"));
//INDRA 10-01-2013				
			}
			jornadasPosteriores = jornadasPosteriores.substring(0, jornadasPosteriores.length() - 1);
			//INDRA 10-01-2013
			/*Obtencion de pedidos por jornada*/
			String sql = "select id_jpicking, count(BO_PEDIDOS.id_pedido) CANT_PEDIDOS " + 
						 "from BO_PEDIDOS " + 
						 "where id_jpicking in (" + jornadasPosteriores + ") " +
						 "group by id_jpicking";		
			ps = con.prepareStatement(sql	+ " WITH UR");
			System.out.println("Jornadas Posteriores : ");
			System.out.println(strSqlJornadasPosteriores);
			rs = ps.executeQuery();
			while (rs.next()) {
				jornadasPedidos.put(rs.getString("ID_JPICKING"), rs.getString("CANT_PEDIDOS"));
			}
			//INDRA 10-01-2013
			/* Obtencion de productos faltantes de jornadas anteriores */
			strSqlProductosFaltantesAnteriores = strSqlProductosFaltantesAnteriores.replaceAll("jornadasAnteriores",jornadasAnteriores);
			
			
			
			ps = con.prepareStatement(strSqlProductosFaltantesAnteriores + " WITH UR");
			System.out.println("Faltantes Anteriores: ");
			System.out.println(strSqlProductosFaltantesAnteriores);
			rs = ps.executeQuery();
			while (rs.next()) {
				productosFaltantesAnteriores = productosFaltantesAnteriores + rs.getString("ID_PRODUCTO") + ",";
			}
			if (productosFaltantesAnteriores.length() > 0) {
				productosFaltantesAnteriores = productosFaltantesAnteriores.substring(0, productosFaltantesAnteriores.length() - 1);
			}else {
				productosFaltantesAnteriores = "0";
			}

			/* Obtencion de listado de faltantes */
//			strSqlProdJornadasActuales = strSqlProdJornadasActuales.replaceAll("jornadasPosteriores", jornadasPosteriores);
			strSqlProdJornadasActuales = strSqlProdJornadasActuales.replaceAll("productosFaltantesAnteriores", productosFaltantesAnteriores);
			strSqlProdJornadasActuales = strSqlProdJornadasActuales.replaceAll("jornadasPosteriores", jornadasPosteriores);
			ps = con.prepareStatement(strSqlProdJornadasActuales + " WITH UR");
			System.out.println("Productos en Jornadas Actuales : ");
			System.out.println(strSqlProdJornadasActuales);
			rs = ps.executeQuery();
			List productosFaltantes = new ArrayList();
			List idProductosFaltantes = new ArrayList();
			/* Jornadas actuales */
			
			String[] jornadas = jornadasPosteriores.split(",");
			
			while (rs.next()) {
				ProductoFaltantesDTO producto = new ProductoFaltantesDTO();
				producto.setIdProducto(rs.getLong("ID_PRODUCTO"));
				producto.setSectorPicking(rs.getString("NOMBRE"));
				producto.setDescripcion(rs.getString("DESCRIPCION"));
				producto.setIdJornada(rs.getLong("ID_JPICKING"));
				producto.setCantidadProductos(rs.getString("CANT_SOLIC"));
				producto.setPresenciaProductosEnJornada(rs.getLong("CANT_PROD_EN_OP_X_JORNADA"));
				producto.setOpsTotalesPorJornada(rs.getLong("TOTAL_OPS_X_JORNADA"));
				producto.setPorcentajePresencia((new Long(Math.round(rs.getDouble("PORC_PRESENCIA")))).toString());
				
				if (!idProductosFaltantes.contains(new Long(rs.getLong("ID_PRODUCTO")))){
					HashMap productoFaltante = new HashMap();
					productoFaltante.put("idProducto", new Long(rs.getLong("ID_PRODUCTO")));
					productoFaltante.put("sectorPicking",rs.getString("NOMBRE"));
					productoFaltante.put("descripcion", rs.getString("DESCRIPCION"));
					//productoFaltante.put("idJornada",new Long(rs.getLong("ID_JPICKING")));
					idProductosFaltantes.add(new Long(rs.getLong("ID_PRODUCTO")));
					productosFaltantes.add(productoFaltante);
				}
				resultadoEstadistica.add(producto);
			}
			//List listaProductosPorJornada = new ArrayList(); 
			
			for(int i = 0; i < productosFaltantes.size(); i++){
				HashMap mapa = new HashMap();
				mapa = (HashMap)productosFaltantes.get(i);
				List subLista = new ArrayList();
				Long idProducto = (Long)mapa.get("idProducto");
				subLista = obtieneSublista(idProducto.longValue(), resultadoEstadistica);
				subLista = rellenaSubLista(jornadas, subLista, jornadasPedidos);
				listaFinal.add(subLista);
			}
			
			listaFinal = ordenarPorPresencia(listaFinal);
			
			resultado.put("jornadaActual",jornadas[0]);
			resultado.put("fechaActual", fechaConsultaActual);
			resultado.put("listaResultado",listaFinal);
			resultado.put("jornadas", jornadas);
			resultado.put("textoHorario", textoHorario);
			
		}catch(Exception e){
         	e.printStackTrace();
         	throw new DAOException(e);
         }finally {
         	close(rs, ps, con);
         }
		return resultado;
         
	}
	
	private List obtieneSublista(long idProducto, List lista){
		List subLista = new ArrayList();
		for (int i = 0; i < lista.size(); i++){
			ProductoFaltantesDTO producto = (ProductoFaltantesDTO)lista.get(i);
			if(producto.getIdProducto() == idProducto){
				subLista.add(producto);
			}
		}
		System.out.println("Producto : " + idProducto);
		System.out.println("Cantidad Encontrada : " + subLista.size());
		return subLista;
	}
	private List rellenaSubLista(String[] jornadas, List lista, HashMap jornadasPedidos){
		List subLista = new ArrayList();
		List jornadasFaltantes = new ArrayList();
		
		for (int i = 0; i < jornadas.length; i++){
			boolean sw = false;
			for (int j = 0; j < lista.size(); j++){
				ProductoFaltantesDTO producto = (ProductoFaltantesDTO)lista.get(j);
				if (producto.getIdJornada() == Long.parseLong(jornadas[i])){
					sw = true;
				}
			}
			if (!sw){
				jornadasFaltantes.add(jornadas[i]);
			}
		}
		List listaParcial = new ArrayList();
		for (int i = 0; i < jornadasFaltantes.size(); i++){
			ProductoFaltantesDTO productoJornada = (ProductoFaltantesDTO)lista.get(0);
			ProductoFaltantesDTO producto = new ProductoFaltantesDTO();
//			long cantidadOPs = productoJornada.getOpsTotalesPorJornada();
			System.out.println("indice : " + i);
			long jornada = Long.parseLong((String)jornadasFaltantes.get(i));
			System.out.println("jornada : " + jornada);
			
			String cantidadOPs = (String)jornadasPedidos.get(String.valueOf(jornada));
			producto.setIdProducto(productoJornada.getIdProducto());
			producto.setIdJornada(jornada);
			producto.setDescripcion(productoJornada.getDescripcion());
			producto.setSectorPicking(productoJornada.getSectorPicking());
			producto.setCantidadProductos("0");
			producto.setOpsTotalesPorJornada(Long.parseLong(cantidadOPs));
			producto.setPorcentajePresencia("0");
			producto.setPresenciaProductosEnJornada(0);
			listaParcial.add(producto);
			
		}
		lista.addAll(listaParcial);
		lista = ordenarLista(lista, jornadas);
		
		
		return lista;
	}
	
	private List ordenarLista(List lista, String[] jornadas){
		List listaOrdenada = new ArrayList();
		for (int i = 0; i < jornadas.length; i++){
			for (int j = 0; j < lista.size(); j++){
				ProductoFaltantesDTO prod = (ProductoFaltantesDTO)lista.get(j);
				if (prod.getIdJornada() == Long.parseLong(jornadas[i])){
					listaOrdenada.add(prod);
					break;
				}
			}
		}
		return listaOrdenada;
	}
	
	private long getHorariosByJornada(long idJornada) throws DAOException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		long idHorarios = 0;
		String sql = "SELECT ID_HOR_PICK " + 
					 "FROM BO_HORARIO_PICK " +
					 "WHERE ID_HOR_PICK = (" +
					 "SELECT ID_HOR_PICK " +
					 "FROM BO_JORNADAS_PICK " + 
					 "WHERE ID_JPICKING = " + String.valueOf(idJornada) + ")";	
		try {
			con = conexionUtil.getConexion();
			ps = con.prepareStatement(sql + " WITH UR");
			rs = ps.executeQuery();
			while (rs.next()) {
				idHorarios = rs.getLong("ID_HOR_PICK");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOException(e);
		} finally {
			close(rs, ps, con);
		}
		return idHorarios;
	}
	
	private List ordenarPorPresencia(List lista){
	
		int largo = lista.size();
		for (int paso = 1; paso < largo; paso++){
			for (int i = 0; i < largo - paso; i++){
				ProductoFaltantesDTO prodA = (ProductoFaltantesDTO)((List)lista.get(i)).get(1);
				ProductoFaltantesDTO prodB = (ProductoFaltantesDTO)((List)lista.get(i+1)).get(1);
				if (Double.parseDouble(prodB.getPorcentajePresencia()) > Double.parseDouble(prodA.getPorcentajePresencia())){
					List aux = (List)lista.get(i);
					lista.set(i, lista.get(i+1));
					lista.set(i+1, aux);
				}
			}
		}
		
		return lista;
	}
	
	private List getHorariosByLocal(String fechaConsulta, long idLocal) throws DAOException{
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		if (fechaConsulta == ""){
			fechaConsulta = "(SELECT CURRENT_DATE FROM SYSIBM.SYSDUMMY1)";
		}
		
		List jornadas = new ArrayList();
		String sql = "SELECT ID_HOR_PICK, HINI, HFIN" + " " + 
					 "FROM BO_HORARIO_PICK"  + " " +
					 "WHERE ID_HOR_PICK IN ("  + " " +
					 "SELECT ID_HOR_PICK FROM BO_JORNADAS_PICK"  + " " +
					 "WHERE ID_LOCAL = " + String.valueOf(idLocal) + " " + 
					 "AND FECHA = " + fechaConsulta + ")";
		try {
			con = conexionUtil.getConexion();
			ps = con.prepareStatement(sql + " WITH UR");
			rs = ps.executeQuery();
			
			while (rs.next()) {
				HashMap jornada = new HashMap();
				jornada.put("idHorario", String.valueOf(rs.getLong("ID_HOR_PICK")));
				jornada.put("horarioInicio", String.valueOf(rs.getLong("HINI")));
				jornada.put("horarioFin", String.valueOf(rs.getLong("HFIN")));
				jornadas.add(jornada);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOException(e);
		} finally {
			close(rs, ps, con);
		}
		return jornadas;
	}

}
