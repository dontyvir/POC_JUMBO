var MENU_ITEMS = [
	[' Inicio', 'ViewHome', null,
		// Submenu	
	],
	[' Catalogo', null, null,
		// Submenu
		[' Categor&iacute;as Web', 'ViewMonCatWeb'],
		[' Productos', 'ViewMonProducts'],				
		[' Mix', 'ViewMonMPV'],
		[' Precios y Bloqueos', 'PreciosIndex'],
		[' Marca Grability','ViewPrdGRPS'],
	],		
	[' Marketing', null, null,
		// Submenu
		[' Monitor de Campa&ntilde;as', 'ViewMonCampanas'],		
		[' Monitor de Elementos', 'ViewMonElementos'],
		[' Monitor de Bolsas', 'ViewMonitorBolsas'],
	],
	[' Clientes', null, null,
		// Submenu
		[' Monitor de Clientes', 'ViewMonClient'],
	],	
	[' Pedidos', null, null,
		// Submenu
		[' Monitor de OP', 'ViewMonOP'],
		[' Monitor de Casos', 'ViewMonOP'],
		//[' Asistencia al Cliente', ''],	
	],
	[' Cotizaciones', null, null,
		// Submenu		
		['Monitor Cotizaciones', 'ViewMonCotizaciones'],		
		//[' Marketing', 'm_pedidos.htm'],		
		//[' Cargas', 'm_pedidos.htm'],		
		//[' Comerciales', 'm_pedidos.htm'],		
	],	
	[' Sistema', null, null,
		// Submenu
		[' Adm. Usuarios', 'ViewMonUsers'],
		[' Adm. Perfiles', 'ViewMonPerfiles'],		
		[' Adm. Comunas', 'ViewListComunas'],		
		[' Adm. Marcas', 'ViewMonMarcas'],
		[' Adm. Empresas', 'ViewMonEmpresa'],
		[' Adm. Sucursales', 'ViewMonSucursal'],
		[' Adm. Compradores', 'ViewMonComprador'],
		[' Adm. Locales', 'ViewMonLocales'],
		[' Adm. Casos', 'ViewDatosConfigCasos'],
		//[' Adm. MKT', 'ViewMonMKT'],		
		//[' Adm. M?dulos', '']
	],
	[' Salir', 'Logoff?url=ViewLogonForm', null,
		// Submenu					
	],	
];
new menu (MENU_ITEMS, MENU_POS);
