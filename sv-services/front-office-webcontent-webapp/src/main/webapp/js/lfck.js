/*!
 * facebook Jumbo JavaScript Library v1.0 alvgutierr@yahoo.es
 */
	//var MY_F_ID='224571767712372'; //jumbologin. TEST Local
	//var MY_F_ID='771405976219346'; //Jumbo TEST oficial
	var MY_F_ID='215277725321164';// Jumbo oficial
        
	jQuery().ready(
		function() {
						
			jQuery("#loginFacebook").click(function(e){
				e.preventDefault();
				closeLBoxWin();
				jQuery('#contenedor_lightbox').hide();
				jQuery('#registra_cliente').hide();
				jQuery('#opcionesRegistro').hide();
				jQuery('#agrega_direccion').hide();
				jQuery('#mensaje_dir_exito').hide();
				jQuery('#loginFck').hide();
				
					FB.login(function(response) {
						if (response.authResponse) {
							 FB.api('/me', function(response_me) {
								 jQuery( "#contenedor").hide();
								 jQuery( "#loading").show();
								 var action="Rfl";
								 var data = "access_token="+response.authResponse.accessToken+"&id="+response_me.id;  								 
								 var bind= {type: "POST",data:data,cache: false,
			                                success:function (data, textStatus, jqXHR){		                                	
			                                	jQuery( "#loading").hide();		                                	
			                                	var status = parseInt(data.status);
												switch(status){
													case 202:
														window.location.href="/FO/UltComprasForm?opcion=2";//data.location;
														break;
													case 406:
														jQuery("#contenedor").show();
														getPhotoFacebook();
														//width:500px; height:354px;
														tb_show('Jumbo.cl','#TB_inline?width=500&height=354&inlineId=hiddenModalContent&modal=true');
														jQuery('#loginFck').show();
														jQuery('#accesoLfck #rut').focus();													
														jQuery('#loginFck_html_username').html(response_me.name);
														break;
													default:
														window.location.href="/FO/LogonForm";
												}   
			                                    	
			                                },
			                                error:function(jqXHR, textStatus, errorThrown){
			                                	jQuery( "#loading").hide();
			                                    alert(errorThrown); 
			                                    window.location.href="/FO/LogonForm";
			                                }
			                            };
								 	jQuery.ajax(action,bind);
						      });
						} else {
							window.location.href="/FO/LogonForm";
						}
					}, {scope: 'email'});
				});

			
			jQuery("#accesoLfck #clave").keydown(function(e){
				if(e.keyCode==13) {
					checkLogin();
				}
			});
			
			jQuery("#accesoLfckHhref").click(function(e){
				checkLogin();
			});
			
			jQuery("#accesoLfckHhrefTestModal").click(function(e){
				tb_show('Jumbo.cl','#TB_inline?width=500&height=354&inlineId=hiddenModalContent&modal=true');
				jQuery('#loginFck').show();
			});
			
			jQuery("#Registrate_loginFck").click(function(e){				
				jQuery('#loginFck').hide();
				showRegistro();
				FB.api('/me', function(response_me) {
					 jQuery('#RegistroSencillo #nombre').val(response_me.first_name);	
					 jQuery('#RegistroSencillo #ape_pat').val(response_me.middle_name);
					 jQuery('#RegistroSencillo #email_reg').val(response_me.email);	
					 jQuery('#RegistroSencillo #cli_rut_reg').focus();
					 jQuery('<input name="accesoLfckfield" id="accesoLfckfield" value="'+response_me.id+'" type="hidden" />').appendTo("#RegistroSencillo");
			     });				
			});
			
			jQuery("#Registrate_loginFck_olvideClave").click(function(e){				
				jQuery('#loginFck').hide();
				closeLBoxWin();
				showOlvidaClaveLayer();			
			});
			
			jQuery("#accesoLfck_closeModal").click(function(e){				
				e.preventDefault();
				closeLBoxWin();
			});
			
		}
	);
	
	
	function checkLogin(){
		
		 var action  = jQuery('#accesoLfck').attr('action'); 
         var data    = jQuery('#accesoLfck').serialize();

         jQuery( "#loading").show();
		 jQuery( "#loginFck").hide();
         //loginFck_loading
		 var bind= {type: "POST",data:data,cache: false,
                    success:function (data, textStatus, jqXHR){		
                    	 	jQuery(data).find('login').each(function() {
                    			var mensaje = jQuery(this).find('mensaje').text();
                    			var destination = jQuery(this).find('destination').text();
                    			if ( mensaje == 'OK' ) {
                    				window.location.href="/FO/UltComprasForm?opcion=2";
                    				return;
                    			} else {                    				
                    				alert(mensaje);
                    				jQuery( "#loading").hide();
                           		 	jQuery( "#loginFck").show();
                           		 
                    				jQuery('#accesoLfck #rut').val('');
                    				jQuery('#accesoLfck #clave').val('');
                    				jQuery('#accesoLfck #rut').focus();
                    				return;
                    			}
                    		});    
                    	 	 
                    },
                    error:function(jqXHR, textStatus, errorThrown){
                		 jQuery("#loading").hide();
                        //alert(errorThrown); 
                        window.location.href="/FO/LogonForm";
                    }
                };
		 		jQuery.ajax(action,bind);
	}
	
	function getPhotoFacebook(){

		FB.api('/me/picture?type=small', function(response_picture) {
			strImg="<img src='"+response_picture.data.url+"' width=\"50px\"  height=\"50px\" />";	
			jQuery('#loginFck_html_picture').html(strImg);
		});	
	}
	
	function logoutFacebook(){
		FB.logout(function(){document.location.reload();});
	}
		