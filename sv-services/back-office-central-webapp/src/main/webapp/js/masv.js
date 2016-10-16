var $j = jQuery.noConflict();
jQuery().ready(function(){
	$j('a#masv').click(function(e){	
		minihomemasv();
	});
	$j('a#bann').click(function(e){	
		minihomebanners();
	});
});

function minihomemasv(){
	$j.get("/JumboBOCentral/ViewMasvCategorias"
		,function(datos){
			$j("div#div_contenedor").html(datos);
			eventos();
	});
}

function minihomebanners(){
	$j.get("/JumboBOCentral/ViewBannersCategorias"
		,function(datos){
			$j("div#div_contenedor").html(datos);
			eventosBanners();
	});
}

function agregarcategoria(){
	$j.get("/JumboBOCentral/ViewBannersCatWebNewForm"
		,function(datos){
			$j("div#div_contenedor").html(datos);
			eventosAgregar();
	});
}

function eventos(){
	$j('#tabla_masv a').click(function(e){	
		$j.get("/JumboBOCentral/MasvCategoriasEdit", {categoriaId: $j(this).attr("id")}
			,function(datos){
				$j("div#div_contenedor").html(datos);
				$j('#contenedor').tabs();
				eventos2();
		});
	});
}

function eventosBanners(){
	$j('#tabla_banners a').click(function(e){	
		$j.get("/JumboBOCentral/BannersCategoriasEdit", {categoriaId: $j(this).attr("id")}
			,function(datos){
				$j("div#div_contenedor").html(datos);
				$j('#contenedor').tabs();
				eventosBanners2();
		});
	});
}

function eventosAgregar(){
	$j('a#bancat_volver').click(function(e){	
		minihomebanners();
	});
	$j('#agregar').click(function(e){
		$j.post("/JumboBOCentral/AddBannerCatWeb", {bca_cat_id: $j("#bca_cat_id").val()}
			,function(datos){
				$j("div#div_contenedor").html(datos);
				eventos();
		});
	});
}


function eventos2(){
	$j('a#masv_volver').click(function(e){	
		minihomemasv();
	});
	
	$j('#activo_masv').change(function(e){	
		$j.post("/JumboBOCentral/MasvCategoriasUpd", {activo_masv: $j(this).val(), categoria_id: $j("#categoria_id").val()}
		);
	});
	
	$j('#activo_banner').change(function(e){	
		$j.post("/JumboBOCentral/MasvCategoriasUpd", {activo_banner: $j(this).val(), categoria_id: $j("#categoria_id").val()}
		);
	});

	$j('div.dbanner').mouseover(function(e){	
		$j(this).css({'border':'1px solid #FFCDA9'});
	});
	
	$j('div.dbanner').mouseout(function(e){	
		$j(this).css({'border':'1px solid #FFFFFF'});
	});
	
	$j('div.dbanner').click(function(e){
		if( $j(this).children().size() == 0){
			id = $j(this).attr('id');
			texto = $j(this).text() + $j(this).val();
			$j(this).html("<input id='b" + id + "' class='dinput' value='" + $j(this).text() + "'/>");
			$j("#b"+id).focus();
			e_input();
		}
	});
	
	$j('a.preview').click(function(e){
		id = $j(this).attr('id');
		bid = id.substring(1);
		texto = $j('#'+bid).text();
		var ban = '<embed height="250" width="392" wmode="transparent" scale="noscale" allowscriptaccess="sameDomain" quality="high" bgcolor="#FFFFFF" name="animationName" id="animationName" src="http://www.jumbo.cl/FO_IMGS/img/banners/cat_padre/';
		ban += texto + '" type="application/x-shockwave-flash"></embed>';
		$j('div#banner_preview').html(ban);
	});
	
	$j("#fec_inicio").dynDateTime({
			ifFormat: "%d-%m-%Y", 
			button: ".next()", //next sibling
			onUpdate: function(e){
							$j.post("/JumboBOCentral/MasvCategoriasUpd", {fecha_inicio: e.date.print("%d-%m-%Y"), categoria_id: $j("#categoria_id").val()})
						}
	});
	$j("#fec_termino").dynDateTime({
			ifFormat: "%d-%m-%Y", 
			button: ".next()", //next sibling
			onUpdate: function(e){
							$j.post("/JumboBOCentral/MasvCategoriasUpd", {fecha_termino: e.date.print("%d-%m-%Y"), categoria_id: $j("#categoria_id").val()})
						}
	});
}

function eventosBanners2(){
	$j('a#banners_volver').click(function(e){	
		minihomebanners();
	});
	
	$j('#bca_estado').change(function(e){	
		$j.post("/JumboBOCentral/BannersCategoriasUpd", {bca_estado: $j(this).val(), categoria_id: $j("#categoria_id").val()}
		);
	});

	$j('div.dbanner').mouseover(function(e){	
		$j(this).css({'border':'1px solid #FFCDA9'});
	});
	
	$j('div.dbanner').mouseout(function(e){	
		$j(this).css({'border':'1px solid #FFFFFF'});
	});
	
	$j('div.dbanner').click(function(e){
		if( $j(this).children().size() == 0){
			id = $j(this).attr('id');
			texto = $j(this).text() + $j(this).val();
			$j(this).html("<input id='b" + id + "' class='dinput' value='" + $j(this).text() + "'/>");
			$j("#b"+id).focus();
			e_input();
		}
	});
	
	$j('a.preview').click(function(e){
		id = $j(this).attr('id');
		bid = id.substring(1);
		texto = $j('#'+bid).text();
		var ban = '<embed height="250" width="392" wmode="transparent" scale="noscale" allowscriptaccess="sameDomain" quality="high" bgcolor="#FFFFFF" name="animationName" id="animationName" src="http://www.jumbo.cl/FO_IMGS/img/banners/cat_padre/';
		ban += texto + '" type="application/x-shockwave-flash"></embed>';
		$j('div#banner_preview').html(ban);
	});
	
	$j("#bca_fch_inicio").dynDateTime({
			ifFormat: "%d-%m-%Y", 
			button: ".next()", //next sibling
			onUpdate: function(e){
							$j.post("/JumboBOCentral/BannersCategoriasUpd", {bca_fch_inicio: e.date.print("%d-%m-%Y"), categoria_id: $j("#categoria_id").val()})
						}
	});
	$j("#bca_fch_termino").dynDateTime({
			ifFormat: "%d-%m-%Y", 
			button: ".next()", //next sibling
			onUpdate: function(e){
							$j.post("/JumboBOCentral/BannersCategoriasUpd", {bca_fch_termino: e.date.print("%d-%m-%Y"), categoria_id: $j("#categoria_id").val()})
						}
	});
}

function e_input(){
	$j('.dinput').blur(function(e){
		var texto = $j(this).val();
		$j(this).parent().html(texto);
	});
	
	$j('.dinput').change(function(e){
		var id = $j(this).attr('id');
		var texto = $j(this).val();
		$j.post("/JumboBOCentral/BannersCategoriasUpd", {banner: id, nombre: texto, categoria_id: $j("#categoria_id").val()}
		);
	});
}