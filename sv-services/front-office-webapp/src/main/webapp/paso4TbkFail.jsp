<html>
<script type="text/javascript">
function cargalo() {
//alert("fallo transback");
	var direccion = '<%=request.getContextPath() %>/Pago?err=1';
	window.parent.cargaErr(direccion);
	
}
</script>
<body onload="cargalo();">
</body>
</html>
