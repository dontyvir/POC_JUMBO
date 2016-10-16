var var_time = '';
var var_out = '';

var $u = jQuery.noConflict();

$u(document).ready(
    function(){
	
		var agent = navigator.userAgent.toLowerCase();
		var is_iphone = (agent.indexOf('iphone') != -1);
		var is_android = (agent.indexOf('android') != -1);
		
		if ( is_iphone || is_android ) {
			menuHoverMobile();
		} else {
			menuHover();
		}        	       
			
    }
);

//function original
function menuHover() {
  var arr_tallas = new Array(0,1,2,3,4,5,6,7,8,9,10,11);
  $u.each(arr_tallas, 
  function(i, i){
	    	$u('#link_'+i).hover(
			function () {						
				if(var_time != ''){
					clearTimeout(var_time);							
				}							
					var_time = window.setTimeout(function(){ 
					$u('.dropdown_'+i).addClass('dropdown_'+i+'_view');	
					$u('.dropdown_'+i).css("display","block");
					}, 500);
					},
			function () {							
				var_out = window.setTimeout(function(){
					$u('.dropdown_'+i).css("display","none");
					$u('.dropdown_'+i).removeClass('dropdown_'+i+'_view');																	
					}, 01);		
						if(var_time != ''){
							clearTimeout(var_time);							
						}
					}
				);
				$u('.dropdown_'+i).hover(
					function () {
						clearTimeout(var_out);						
					},
					function () {}
				);                          
	        } 
	    );	
	}
	
	
	//function smartphone
	function menuHoverMobile() {
	    var arr_tallas = new Array(0,1,2,3,4,5,6,7,8,9,10,11);
	    jQuery.each(arr_tallas, function(i, i){	
	    	jQuery('#link_'+i+' a:first').attr('href','#');
	    	jQuery('#link_'+i).unbind('mouseenter mouseleave');
			jQuery('#link_'+i+' a:first').click(function(event) {
				event.preventDefault();
				if(!jQuery('.dropdown_'+i).hasClass('dropdown_'+i+'_view')) {
					jQuery.each(arr_tallas, 
						function(j, j){jQuery('.dropdown_'+j).css("display","none");jQuery('.dropdown_'+j).removeClass('dropdown_'+j+'_view');});
					if(var_time != '')
						clearTimeout(var_time);							
					jQuery('.dropdown_'+i).addClass('dropdown_'+i+'_view');
					jQuery('.dropdown_'+i).css("display","block");
				} else {
					var_out = window.setTimeout(function(){jQuery('.dropdown_'+i).css("display","none");jQuery('.dropdown_'+i).removeClass('dropdown_'+i+'_view');}, 01);
					if(var_time != '')
	                    clearTimeout(var_time);
				}
			});	        	       
	    });
	}