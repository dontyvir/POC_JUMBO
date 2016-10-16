// Menu

/*
var MENU_ITEMS = [
	['Inicio','/WebFOVE/ViewMenu',null],
	['Cotizaciones',null,null,
		['Monitor','/WebFOVE/ViewSearchCotizaciones'],
		['Nueva Cotización','/WebFOVE/ViewNewCotizacionP1']
	],
	['Direcciones', null, null, 
		['Despacho',null,null,
			['Monitor', '/WebFOVE/ViewListDirecDespacho', null]
		],
		['Facturación',null,null,
			['Monitor', '/WebFOVE/ViewListDirecFact', null]
		]
	],
	['Compradores',null,null,
		['Monitor', '/WebFOVE/ViewListCompradores', null]
	],
	['Salir', '/WebFOVE/Logoff', null ]
];

var MENU_ALL = MENU_ITEMS.concat( MENU_ITEMS2 );

*/

var MENU_INICIO = ['Inicio','/WebFOVE/ViewMenu',null];
var MENU_SALIR = ['Salir', '/WebFOVE/Logoff', null ];
var MENU_COTIZACIONES = ['Cotizaciones',null,null,
							['Listado Cotizaciones','/WebFOVE/ViewSearchCotizaciones'],
							['Nueva Cotización','/WebFOVE/ViewNewCotizacionP1']
						];

var MENU_DIRECCIONES = ['Direcciones', null, null, 
							['Despacho', '/WebFOVE/ViewListDirecDespacho', null],
							['Facturación','/WebFOVE/ViewListDirecFact', null]
						];

var MENU_COMPRADORES = ['Compradores',null,null,
							['Listado de compradores', '/WebFOVE/ViewListCompradores', null],
							['Nuevo Comprador', '/WebFOVE/ViewWizardCompradores', null]
						];
var MENU_SUCURSALES = ['Sucursales',null,null,
							['Listado de sucursales', '/WebFOVE/ViewListSucursales', null],
							['Nueva sucursal', '/WebFOVE/ViewFormSucursal', null]
						];

if( tipo_usuario == 1 )
	var MENU_ALL = [ MENU_INICIO, MENU_COTIZACIONES, MENU_COMPRADORES, MENU_SUCURSALES, MENU_SALIR ];
else
	var MENU_ALL = [ MENU_INICIO, MENU_COTIZACIONES, MENU_SALIR ];

new menu (MENU_ALL, MENU_POS);