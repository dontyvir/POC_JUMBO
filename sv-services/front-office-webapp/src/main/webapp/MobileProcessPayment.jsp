<html>
<script type="text/javascript">
<%
String tipoAutorizacion = request.getParameter("tipoAutorizacion");
String numeroTarjeta = request.getParameter("numeroTarjeta");
String rutCliente = request.getParameter("rutCliente");
String usoClave = request.getParameter("usoClave");
String glosaRespuesta = request.getParameter("glosaRespuesta");
String codigoRespuesta = request.getParameter("codigoRespuesta");
%>

function cargalo() {
	//alert("ok tarjeta mas");
	var direccion = '<%=request.getContextPath() %>/MobileOrderComplete?tipoAutorizacion=<%=tipoAutorizacion%>&numeroTarjeta=<%=numeroTarjeta%>&rutCliente=<%=rutCliente%>&usoClave=<%=usoClave%>&glosaRespuesta=<%=glosaRespuesta%>&codigoRespuesta=<%=codigoRespuesta%>';
	//alert(direccion)
	window.parent.cargaMas(direccion, '<%=tipoAutorizacion%>','<%=numeroTarjeta%>','<%=rutCliente%>','<%=usoClave%>','<%=glosaRespuesta%>','<%=codigoRespuesta%>');
	
}
</script>
<body onLoad="cargalo();">
</body>
</html>
