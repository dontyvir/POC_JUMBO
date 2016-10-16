<html>
<script type="text/javascript">
function cargalo() {
//alert("ok tbk");
	var direccion = '<%=request.getContextPath() %>/OrderComplete';
	window.parent.carga(direccion);
}
</script>
<body onload="cargalo();">
</body>
</html>
