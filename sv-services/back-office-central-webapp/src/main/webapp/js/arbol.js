var $j = jQuery.noConflict();
jQuery().ready(function(){
	$j('a#arbol').click(function(e){	
		$j.get("/JumboBOCentral/ViewArbolCategorias"
			,function(datos){
				$j("div#div_arbol").html(datos);
				eventos();
		});
	});
});


function eventos(){
		$j("#div_tree").tree({
			data : {
				type : "json",
				json : xxx
			},
			lang : {
			        new_node    : "Nueva Categoria",
			        loading     : "Cargando..."
			},
			cookies : {
				prefix : "tree1", opts : { path : '/' }
			},
			rules : {
			        creatable : ["cat"],
			        publicado : ["pub"]
			},

			callback : {
				onopen      : function(NODE, TREE_OBJ) { },
				onchange : function (NODE, TREE_OBJ) {
if(TREE_OBJ.settings.ui.theme_name == "checkbox") {
 var $jthis = $j(NODE).is("li") ? $j(NODE) : $j(NODE).parent();
 if($jthis.children("a.unchecked").size() == 0) {
 TREE_OBJ.container.find("a").addClass("unchecked");
 }
 $jthis.children("a").removeClass("clicked");
 if($jthis.children("a").hasClass("checked")) {
 $jthis.find("li").andSelf().children("a").removeClass("checked").removeClass("undetermined").addClass("unchecked");
 var state = 0;
 }
 else {
 $jthis.find("li").andSelf().children("a").removeClass("unchecked").removeClass("undetermined").addClass("checked");
 var state = 1;
 }
 $jthis.parents("li").each(function () {
 if(state == 1) {
 if($j(this).find("a.unchecked, a.undetermined").size() - 1 > 0) {
 $j(this).parents("li").andSelf().children("a").removeClass("unchecked").removeClass("checked").addClass("undetermined");
 return false;
 }
 else $j(this).children("a").removeClass("unchecked").removeClass("undetermined").addClass("checked");
 }
 else {
 if($j(this).find("a.checked, a.undetermined").size() - 1 > 0) {
 $j(this).parents("li").andSelf().children("a").removeClass("unchecked").removeClass("checked").addClass("undetermined");
 return false;
 }
 else $j(this).children("a").removeClass("checked").removeClass("undetermined").addClass("unchecked");
 }
 });
 }
 }
			},
			
			ui: {
				theme_name : "checkbox",
				context : [
					    	{
					    	    id      : "publicar",
					    	    label   : "Publicar",
					    	    icon    : "f.png",
					    	    visible : function (NODE, TREE_OBJ) {
					    	        return NODE.attr('pub') != 'si';
					    	    },
					    	    action  : function (NODE, TREE_OBJ) {
					    	    	$j(NODE).children("a").css("background-image","url(./source/themes/default/f.png)");
					    	    	NODE.attr('pub','si');
					    	    }
    						},
    						{
					    	    id      : "despublicar",
					    	    label   : "Despublicar",
					    	    icon    : "despub.png",
					    	    visible : function (NODE, TREE_OBJ) {
					    	    	return NODE.attr('pub') != 'no';
					    	    },
					    	    action  : function (NODE, TREE_OBJ) {
					    	    	$j(NODE).children("a").css("background-image","url(./source/themes/default/despub.png)");
					    	    	NODE.attr('pub','no');
					    	    	$j(NODE).children("ul").children("li").children("a").css("background-image","url(./source/themes/default/despub.png)");
					    	    	$j(NODE).children("ul").children("li").attr("pub","no");
					    	    }
    						},
    						"separator",
    									{
							                id      : "create",
							                label   : "Create",
							                icon    : "create.png",
							                visible : function (NODE, TREE_OBJ) { if(NODE.length != 1) return false; return TREE_OBJ.check("creatable", NODE); },
							                action  : function (NODE, TREE_OBJ) {
							                				TREE_OBJ.create({ "attributes" : { "rel":"sub", "id" : "nuevo"  }, "data":{"icon":"","title":"Nueva categoria"}}, TREE_OBJ.get_node(NODE));
							                		}
							            },
							            {
							                id      : "rename",
							                label   : "Rename",
							                icon    : "rename.png",
							                visible : function (NODE, TREE_OBJ) { if(NODE.length != 1) return false; return TREE_OBJ.check("renameable", NODE); },
							                action  : function (NODE, TREE_OBJ) { TREE_OBJ.rename(); }
							            },
							            {
							                id      : "delete",
							                label   : "Delete",
							                icon    : "remove.png",
							                visible : function (NODE, TREE_OBJ) {
							                	var ok = true;
							                	$j.each(NODE, function () {
							                		if(TREE_OBJ.check("deletable", this) == false)
							                			ok = false;
							                		return false;
							                	});
							                	return ok;
							                },
							                action  : function (NODE, TREE_OBJ) {
							                	$j.each(NODE, function () {
							                		TREE_OBJ.remove(this);
							                	});
							                }
							            }

    					]
				}
		});
	
}
