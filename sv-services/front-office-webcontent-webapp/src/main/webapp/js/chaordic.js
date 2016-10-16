var muestra_chaordic_subcategoria;
var muestrachaordic;
function chaordicHome(	ses_cli_id		,	
						ses_cli_nombre	,
						ses_cli_email	)
{		

	if ((typeof(ses_cli_nombre)!== 'undefined') && Trim(ses_cli_nombre)=='Invitado' ){
		window.chaordic_meta = {
			    "page": {
		    			"name": "home"
				}
		};

		
	}else{
		window.chaordic_meta = {
			    "page": {
		    			"name": "home"
				},
				"user": {
						"id"					: ses_cli_id			, 
						"name"					: ses_cli_nombre     	, 
						"email"					: ses_cli_email       	, 
						"allow_mail_marketing"	: false                 
				}
		};
	}

	chaordic.initialize();
	return false;

}


function chaordicCategorias(nombreCategoria				,
							ses_cli_id					,
							ses_cli_nombre				,
							ses_cli_email				){ 

	var jerarquia = '["'+nombreCategoria+'"]';
	var arr = JSON.parse(jerarquia);
	
	if ((typeof(ses_cli_nombre)!== 'undefined') && Trim(ses_cli_nombre)=='Invitado' ){
		window.chaordic_meta = {
				"page"		: {
		        "name"		: "category",
		        "categories": arr
				}
		};
	}else{
		window.chaordic_meta = {
				"page"		: {
		        "name"		: "category",
		        "categories": arr
				},
				   "user": {
				        "id"					: ses_cli_id			, 
				        "name"					: ses_cli_nombre     	, 
				        "email"					: ses_cli_email       	, 
				        "allow_mail_marketing"	: false                 
				    }
		};
	}

	chaordic.initialize();
	return false;

}

function chaordicSubCategorias(nombreSubCategoria		,
		                       nombreCategoria			,
		                       ses_cli_id				,
		                       ses_cli_nombre			,
		                       ses_cli_email			){ 

	var jerarquia = '["'+nombreCategoria+'","'+nombreSubCategoria+'"]';
	var arr = JSON.parse(jerarquia);
	
	if ((typeof(ses_cli_nombre)!== 'undefined') && Trim(ses_cli_nombre)=='Invitado' ){
		window.chaordic_meta = {
			    "page": {
		        "name": "subcategory",
		        "categories": 	arr
			    }
			};
	}else{
		window.chaordic_meta = {
			    "page": {
		        "name": "subcategory",
		        "categories": 	arr
			    },
				   "user": {
				    "id"					: ses_cli_id			, 
			        "name"					: ses_cli_nombre     	, 
			        "email"					: ses_cli_email       	, 
			        "allow_mail_marketing"	: false                 
			    }
			};
	}
	chaordic.initialize();
	return false;
}

function chaordicSubSubCategorias(	nombreSubSubCategoria	,
									nombreSubCategoria		,
							        nombreCategoria			,
							        ses_cli_id				,
							        ses_cli_nombre			,
							        ses_cli_email			){ 
	
	var jerarquia = '["'+nombreCategoria+'","'+nombreSubCategoria+'","'+nombreSubSubCategoria+'"]';
	var arr = JSON.parse(jerarquia);
	
	if ((typeof(ses_cli_nombre)!== 'undefined') && Trim(ses_cli_nombre)=='Invitado' ){
	
		window.chaordic_meta = {
			"page": {
			"name": "subcategory",
			"categories": 	arr
					}
		};
	}else{
		window.chaordic_meta = {
			"page": {
			"name": "subcategory",
			"categories": 	arr
			},
			"user": {
				"id"					: ses_cli_id			, 
				"name"					: ses_cli_nombre     	, 
				"email"					: ses_cli_email       	, 
				"allow_mail_marketing"	: false                 
			}
		};
	}

	chaordic.initialize();
	return false;
}

function chaordicFichaProducto( nombreSubSubCategoria	,
								nombreSubCategoria		,
						        nombreCategoria			,
						        id_prod					,
						        ses_cli_id				,
						        ses_cli_nombre			,
						        ses_cli_email			){

	var jerarquia = '["'+nombreCategoria+'","'+nombreSubCategoria+'","'+nombreSubSubCategoria+'"]';
	var arr = JSON.parse(jerarquia);
	
	if ((typeof(ses_cli_nombre)!== 'undefined') && Trim( ses_cli_nombre )=='Invitado' ){
		
		window.chaordic_meta = {
		        "page"		: {
		        "name"		: "product",
				"categories": 	arr
		    },
		    "product"	: {
		    	"id"	: id_prod
		    }
		};
	}else{
		window.chaordic_meta = {
		        "page"		: {
		        "name"		: "product",
				"categories": 	arr
		    },
		    "product"	: {
		    	"id"	: id_prod
		    },
			"user": {
				"id"					: ses_cli_id			, 
				"name"					: ses_cli_nombre     	, 
				"email"					: ses_cli_email       	, 
				"allow_mail_marketing"	: false                 
			}
		};
	}
	//chaordic.initialize();
	return false;
}

function  chaordicActualizaCarro(	id_cart		,
									detalleItems){
	var items = '';
	var i = 0;
	var arr = new Array();
	
	if(null != detalleItems.length){
		for( i = 0 ; i < detalleItems.length ; i++){
			if(i != 0){
				items += ',';
			}
			items += '{"product":{"id":"'+detalleItems[i][0]+'","price":'+detalleItems[i][1]+'},"quantity":'+detalleItems[i][2]+'}';
		}
		arr = JSON.parse('['+items+']');
	}

	chaordic.push(	["updateCart", {
						"id"	: id_cart,
						"items"	: arr
					}]
	);
}

function  chaordicCarroCompra(	ses_cli_id		,	
								ses_cli_nombre	,
								ses_cli_email	,
								detalleItems 	){
	var items = '';
	var i = 0;
	var arr = new Array();
	
	if(null != detalleItems.length){
		for( i = 0 ; i < detalleItems.length ; i++){
			if(i != 0){
				items += ',';
			}
			items += '{"product":{"id":"'+detalleItems[i][0]+'","price":'+detalleItems[i][1]+'},"quantity":'+detalleItems[i][2]+'}';
		}
		arr = JSON.parse('['+items+']');
	}
	
	if ((typeof(ses_cli_nombre)!== 'undefined') && Trim( ses_cli_nombre )=='Invitado' ){
		
		window.chaordic_meta = {
		        "page"		: {
		        "name"		: "cart"
		    },
		    "cart": {
		    	"items" : arr
		    }
		};
		
	}else{

		window.chaordic_meta = {
		        "page"		: {
		        "name"		: "cart"
		    },
		    "cart": {
		    	"items" : arr
		    }
		    ,
		    "user": {
		        "id"					: ses_cli_id			, 
		        "name"					: ses_cli_nombre     	, 
		        "email"					: ses_cli_email       	,  
		        "allow_mail_marketing"	: false           		
		    }		    
		};
	}

	//chaordic.initialize();
	return false;
}

function chaordicCheckOut(	ses_cli_id		,	
							ses_cli_nombre	,
							ses_cli_email	)
{

	window.chaordic_meta = {
		"page"	: {
			"name"	: "checkout"
		},
		"user": {
			"id"					: ses_cli_id			, 
			"name"					: ses_cli_nombre     	, 
			"email"					: ses_cli_email       	, 
			"allow_mail_marketing"	: false                
		}
	};

	//chaordic.initialize();
	return false;

}	

function chaordicConfirmacion(	ses_cli_id		,	
								ses_cli_nombre	,
								ses_cli_email	,
								id_transaccion	,
								detalleItems 	){
	var items = '';
	var i = 0;
	var arr = new Array();
	
	if(null != detalleItems.length){
		for( i = 0 ; i < detalleItems.length ; i++){
			if(i != 0){
				items += ',';
			}
			items += '{"product":{"id":"'+detalleItems[i][0]+'","price":'+detalleItems[i][1]+'},"quantity":'+detalleItems[i][2]+'}';
		}
		arr = JSON.parse('['+items+']');
	}

	window.chaordic_meta = {
		"page"		: {
			"name"		: "confirmation"
		},
		"transaction": {
			"id": id_transaccion	,
			"items": arr
		}
		,
		"user": {
			"id"					: ses_cli_id			, 
			"name"					: ses_cli_nombre     	, 
			"email"					: ses_cli_email       	, 
			"allow_mail_marketing"	: false                 
		}
	};

	//chaordic.initialize();
	return false;

}