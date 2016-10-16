var MENU_ITEMS = [
	[' Inicio', 'home.htm', null,
		// Submenu
		//[' Observaciones', 'observaciones.htm'],		
	],
	[' Pedidos', null, null,
		// Submenu		
		[' Monitor de Pedidos', '/BO/BOL/ViewMonitorPedidos'],
		[' Monitor de Casos', 'ViewMonitorCasos'],		
	],	
	[' Picking', null, null,
		// Submenu
		[' Monitor de Jornadas', 'mon_jornada.htm'],
		[' Monitor de Rondas', 'mon_rondas.htm'],
		[' Crear Rondas', 'crea_ronda1.htm'],		
	],		
	[' Despacho', null, null,
		// Submenu
		[' Uno', null],
		[' Dos', null],
		[' Tres', null],			
	],
	[' Reportes', null, null,
		// Submenu
		[' Reporte 1', null],
		[' Reporte 2', null],		
		[' Reporte 3', null],		
	],	
	[' Utilidades', null, null,
		// Submenu
		[' Datos Personales', ''],
		[' Ayuda', ''],		
	],
	[' Sistema', null, null,
		// Submenu
		[' Adm. Usuarios', ''],
		[' Adm. Perfiles', ''],		
		[' Adm. M?dulos', ''],		
		[' Adm. Jornadas de Picking', ''],				
		[' Adm. Cal. de Despacho', ''],
		[' Adm. Secciones Local', ''],		
		[' Adm. Zonas Local', ''],
	],
	[' Salir', '/BO/BOL/Logoff?url=/BO/BOL/LogonForm', null,
		// Submenu
		//[' Adm. Usuarios', ''],						
	],	
];
new menu (MENU_ITEMS, MENU_POS);