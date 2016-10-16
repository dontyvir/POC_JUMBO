var cotizaciones = ['Cotizaciones', '', null, null, null,   // a folder item
        ['Nueva Cotización', '', '/WebFOVE/ViewNewCotizacionP1', null, null],
        ['Listado Cotizaciones', '', '/WebFOVE/ViewSearchCotizaciones', null, null],
        ['Pedidos por pagar', '', '/WebFOVE/ViewPedidosPorPagar', null, null]
    ];
    
var compradores = ['|  Compradores', '', null, null, null,   // a folder item
        ['Nuevo Comprador', '', '/WebFOVE/ViewWizardCompradores', null, null],  // a menu item
	['Listado de compradores', '', '/WebFOVE/ViewListCompradores', null, null]
    ];
    
var sucursales = ['|  Sucursales', '', null, null, null,   // a folder item
        ['Nueva sucursal', '', '/WebFOVE/ViewFormSucursal', null, null],  // a menu item
	['Listado de sucursales', '', '/WebFOVE/ViewListSucursales', null, null]
    ];
    
var direcciones = ['Direcciones', '', null, null, null,   // a folder item
        ['Despacho', '', '/WebFOVE/ViewListDirecDespacho', null, null],  // a menu item
	['Facturación', '', '/WebFOVE/ViewListDirecFact', null, null]
    ];

if (tipo_usuario == 1) {
	var myMenu = [cotizaciones,compradores,sucursales];
} else {
	var myMenu = [cotizaciones];	
}

    
