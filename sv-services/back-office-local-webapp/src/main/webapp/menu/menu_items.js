var MENU_ITEMS = [
	['Inicio', 'ViewHome', null,
		// Submenu
		//[' Observaciones', 'observaciones.htm'],		
	],
	['Pedidos', null, null,
		// Submenu		
		[' Monitor de Pedidos', 'ViewMonitorPedidos'],	
		[' Monitor Pagos', 'ViewMonitorTrxMp'],	
		[' Monitor de Casos', 'ViewMonitorCasos'],
		[' Monitor de Bolsas', 'ViewMonitorBolsas'],
	],	
	['Picking', null, null,
		// Submenu
		[' Monitor de Jornadas', 'ViewMonitorJornadas'],
		[' Monitor de Rondas', 'ViewMonitorRondas'],
		//[' Crear Rondas', 'ViewCrearRonda'],				
	],
	['Despacho', null, null,
		// Submenu		
		[' Monitor Despachos', 'ViewResumenJornada'],
		[' Pedido Jumbo VA', 'ViewAgregaPedidoJumboVA'],
		[' Pedido Caso', 'ViewAgregaPedidoCaso'],
		[' Monitor Rutas', 'ViewMonitorRutas'],
	],
	['Reportes', null, null,
		['Asociar encargados - pickeadores', 'Asociar_EncargadoPickeadorView'],
		['Seguimiento de dotación', 'Asistencia_SeguimientoDiarioView'],
		['Cuadratura de transporte', 'CuadraturaTransporteView'],
		//INDRA 05-12-2012
		['Informe faltantes', 'InformeFaltantesView']
		//INDRA 05-12-2012
		// Submenu
		//[' Reporte 1', null],
		//[' Reporte 2', null],		
		//[' Reporte 3', null],		
	],	
	['Utilidades', null, null,
		// Submenu
		//[' Datos Personales', ''],
		//[' Ayuda', ''],		
	],
	['Sistema', null, null,
		// Submenu
		[' Adm. Jornadas de Picking', 'ViewJornadasPicking'],				
		[' Adm. Cal. de Despacho', 'ViewCalDespacho'],
		[' Adm. Cal.Despacho Masivo', 'ViewCalDespMasivo'],
		[' Adm. Zonas Despacho', 'ViewZonasDespacho'],
		[' Adm. Sectores Local', 'ViewSectoresLocal'],
		[' Adm. Transporte', 'ViewMantenedorTransporte']
	],
	['Salir', 'Logoff?url=ViewLogonForm', null,
		// Submenu
		//[' Adm. Usuarios', ''],						
	],	
];
new menu (MENU_ITEMS, MENU_POS);