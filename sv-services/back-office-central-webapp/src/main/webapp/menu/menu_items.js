var MENU_ITEMS = [
	[' Inicio', 'ViewHome', null
		// Submenu
	],
	[' Catalogo', null, null,
		// Submenu
		[' Categor&iacute;as Web', 'ViewMonCatWeb'],
		[' Productos', 'ViewMonProducts'],
		[' Mix', 'ViewMonMPV'],
		[' Precios y Bloqueos', 'PreciosIndex'],
		[' Marca APP Móvil','ViewPrdGRPS']
	],
	[' Marketing', null, null,
		// Submenu
		[' Monitor de Eventos', 'ViewMonEventos'],
		[' Monitor de Promociones', 'ViewMonPromos'],
		[' Monitor de Campa&ntilde;as', 'ViewMonCampanas'],
		[' Monitor de Elementos', 'ViewMonElementos'],
		[' Monitor de Destacados', 'ViewMonDestacados'],
		[' Monitor de Bolsas', 'ViewMonitorBolsas'],
		[' Administrador Home', 'ViewAdmHome'],
		[' Cupones de Descuentos', 'ViewCuponDescuentos']
	],
	[' Clientes', null, null,
		// Submenu
		[' Monitor de Clientes', 'ViewMonClient'],
		[' Puntajes de Reclamo', 'ViewReclamosClientes'],
		[' Lista blanca de Clientes', 'ViewRutsConfiablesForm'],
		[' Lista negra de Productos', 'ViewListaNegraProductos']
		//[' Indexar Clientes', 'ViewIndexacionBoletas']
	],
	[' Pedidos', null, null,
		// Submenu
		[' Monitor de OP', 'ViewMonOP'],
		[' Monitor de Casos', 'ViewMonCasos'],
		[' Sustitutos no reconocidos', 'ViewSustitutosNoReconocidos'],
		[' Monitor de Despacho', 'ViewMonDespacho'],
		[' OP Pendientes', 'ViewMonOpPendientes'],
		[' Liberar OP', 'ViewLiberarOP']		
		//[' Asistencia al Cliente', ''],
	],
	[' Cotizaciones', null, null,
		// Submenu
		['Monitor Cotizaciones', 'ViewMonCotizaciones']
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
		[' Wizard Empresas', 'ViewWizardEmpInicio0'],
		[' Adm. Locales', 'ViewMonLocales'],
		[' Adm. Casos', 'ViewDatosConfigCasos'],
		[' Adm. Mail S&F', 'ViewAdmMailFaltantes'],
		[' Adm. S&F', 'ViewSustitutosYFaltantesForm'],
		[' Adm. Listas', 'ViewConfigListasEspeciales'],
		[' Adm. Despacho', 'ViewMantenedorDespacho'], 
		//indra
		[' Adm. Parametros', 'ViewMantenedorParametros']
		//indra
		//[' Adm. MKT', 'ViewMonMKT'],
		//[' Adm. M?dulos', '']
	],
	[' Informes', null, null,
		// Submenu
		//[' Informes', 'ViewInformes'],
	    [' Informes Productividad', 'InformeManualProductividad'],
		[' Planilla Transporte', 'ViewGeneraPlanillaTracking'],
		[' Modificación de Precios', 'ViewInformeModPrecios'],
		[' Informe Prod Sin Stock', 'ViewInformeProdSinStock']
	],
	[' Salir', 'Logoff?url=ViewLogonForm', null
		// Submenu
],
];
new menu (MENU_ITEMS, MENU_POS);
