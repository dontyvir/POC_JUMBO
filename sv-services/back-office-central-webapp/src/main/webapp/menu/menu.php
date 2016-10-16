<link rel="stylesheet" href="../../menu/menu.css">
<script language="JavaScript" src="../../menu/menu.js"></script>
<!-- files with geometry and styles structures -->
<script language="JavaScript" src="../../menu/menu_tpl.js"></script>
<script>
<?/*
$lista_mod = get_modulos();

if( $lista_mod == '' )
    $lista_mod = -1;

$query_1 = "select distinct mod_id, mod_url, mod_nombre from permisosxmodulo, modulos where mod_id = pemo_mod_id and pemo_mod_id in ( $lista_mod ) and mod_estado = 1 and mod_padre_id is NULL order by mod_orden, mod_nombre";

$db_1 = tep_db_query($query_1);

echo "var MENU_ITEMS = ["; //Abre js

while( $res_1 = tep_db_fetch_array( $db_1 ) ) {

	$query_2 = "select distinct mod_id, mod_url, mod_nombre from permisosxmodulo, modulos where mod_id = pemo_mod_id and pemo_mod_id in ( $lista_mod ) and mod_padre_id = ".$res_1['mod_id']." and mod_estado = 1 order by mod_orden, mod_nombre";

    $db_2 = tep_db_query($query_2);

    if( tep_db_num_rows($db_2) == 0 )  // No tiene hijos
		$link="'".$res_1['mod_url']."'";
	else
		$link="null";
*/
?>
/*
	['&nbsp;<?= $res_1['mod_nombre'] ?>', <?= $link ?>, null,

		// Submenu
		<? while( $res_2 = tep_db_fetch_array( $db_2 ) ) { ?>

		['&nbsp;<?= $res_2['mod_nombre'] ?>', '<?= $res_2['mod_url'] ?>'],
		<? } // End While ?>

	],
*/
<?
/*
	}//End while1
	echo "];"; // cierra js
*/
?>


var MENU_ITEMS = [
	['&nbsp;Inicio', '../home/index.php', null,
		// Submenu
	],
	['&nbsp;Sistema', null, null,
		// Submenu
		['&nbsp;Administración de módulos', '../adm_mod/adm_mod_01.php'],
		['&nbsp;Administración de perfiles y usuarios', '../adm_usr/adm_usr_01.php'],
		['&nbsp;Administración de usuarios', '../adm_musr/adm_musr_01.php'],
		['&nbsp;Parámetros del sitio', '../var_glo/glo_01.php'],
	],
	['&nbsp;Sitio', null, null,
		// Submenu
		['&nbsp;Editor de Contenidos', '../edi_text/edi_01.php'],
		['&nbsp;Centro de Ayuda', '../adm_faq/index.php'],
		['&nbsp;Contactos Web', '../adm_contactos/index.php'],
		['&nbsp;Administrador de Oficinas', '../adm_oficinas/index.php'],
		['&nbsp;Administrador de Proyectos', '../adm_proyectos/index.php'],
		['&nbsp;Administrador de Convenios', '../adm_convenios/index.php'],
		['&nbsp;Administrador de Empresas', '../adm_empresas/index.php'],
		['&nbsp;Administrador de Tipos', '../adm_tipos/index.php'],
		['&nbsp;Administrador de Menús', '../adm_menus/index.php'],
		['&nbsp;Administración de Consejos', '../adm_consejos/index.php'],
	],
	['&nbsp;Utilidades', null, null,
		// Submenu
		['&nbsp;Ficha personal', '../fic_per/fic_per_01.php'],
	],
	['&nbsp;Salir', '../start/logout_01.php', null,
		// Submenu
	],
];



new menu (MENU_ITEMS, MENU_POS);
</script>