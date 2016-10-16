
--************************ COMANDOS PARA BOL ***********************************

--************* SOLO PEDIDOS JUMBO VA ******************
insert into bodba.bo_comandos 
(id_comando,nombre_clase, descripcion, seguridad, activo)
values
(480,'ViewAgregaPedidoJumboVA','Agregar Pedido tipo Jumbo VA [BOL]','S','A');

insert into bodba.bo_comandos 
(id_comando,nombre_clase, descripcion, seguridad, activo)
values
(487,'TraeDatosDePedidoJumboVA','Trae datos de Pedido VA [BOL]','S','A');



--************* SOLO PEDIDOS CASO ******************
insert into bodba.bo_comandos 
(id_comando,nombre_clase, descripcion, seguridad, activo)
values
(481,'ViewAgregaPedidoCaso','Agregar Pedido tipo Casos [BOL]','S','A');

insert into bodba.bo_comandos 
(id_comando,nombre_clase, descripcion, seguridad, activo)
values
(482,'TraeDatosDeCaso','Traer datos de un caso [BOL]','S','A');

--************* PEDIDOS CASOS Y JUMBO VA ******************
insert into bodba.bo_comandos 
(id_comando,nombre_clase, descripcion, seguridad, activo)
values
(483,'ComunasByRegion','Devuelve html con comunas [BOL]','S','A');

insert into bodba.bo_comandos 
(id_comando,nombre_clase, descripcion, seguridad, activo)
values
(484,'ZonasByComuna','Devuelve html con zonas [BOL]','S','A');

insert into bodba.bo_comandos 
(id_comando,nombre_clase, descripcion, seguridad, activo)
values
(485,'JornadasDespacho','Jornadas de Despacho [BOL]','S','A');

insert into bodba.bo_comandos 
(id_comando,nombre_clase, descripcion, seguridad, activo)
values
(486,'AgregaPedidoExterno','Agrega Pedido Externo [BOL]','S','A');

--******************** MONITOR DE DESPACHO ********************

insert into bodba.bo_comandos 
(id_comando,nombre_clase, descripcion, seguridad, activo)
values
(488,'TraeChoferesTransporte','Trae los choferes [BOL]','S','A');

insert into bodba.bo_comandos 
(id_comando,nombre_clase, descripcion, seguridad, activo)
values
(489,'TraePatentesTransporte','Trae las patentes [BOL]','S','A');

insert into bodba.bo_comandos 
(id_comando,nombre_clase, descripcion, seguridad, activo)
values
(490,'TraeFonosTransporte','Trae los fonos transporte [BOL]','S','A');

insert into bodba.bo_comandos 
(id_comando,nombre_clase, descripcion, seguridad, activo)
values
(491,'AgregaRutaDespacho','Agrega Ruta Despacho [BOL]','S','A');

insert into bodba.bo_comandos 
(id_comando,nombre_clase, descripcion, seguridad, activo)
values
(492,'AgregarPedidoARuta','Agregar Pedido a Ruta [BOL]','S','A');

--******************** MONITOR DE RUTAS ********************

insert into bodba.bo_comandos 
(id_comando,nombre_clase, descripcion, seguridad, activo)
values
(493,'ViewMonitorRutas','Monitor de Rutas [BOL]','S','A');

insert into bodba.bo_comandos 
(id_comando,nombre_clase, descripcion, seguridad, activo)
values
(494,'ViewRuta','Detalle de Ruta [BOL]','S','A');

insert into bodba.bo_comandos 
(id_comando,nombre_clase, descripcion, seguridad, activo)
values
(495,'EliminarPedidoDeRuta','Eliminar Pedido de Ruta [BOL]','S','A');

insert into bodba.bo_comandos 
(id_comando,nombre_clase, descripcion, seguridad, activo)
values
(496,'AnularRuta','Anular Ruta [BOL]','S','A');

insert into bodba.bo_comandos 
(id_comando,nombre_clase, descripcion, seguridad, activo)
values
(497,'ActivarRuta','Activar Ruta [BOL]','S','A');

insert into bodba.bo_comandos 
(id_comando,nombre_clase, descripcion, seguridad, activo)
values
(498,'ViewHojaRuta','Imprimir Hoja de Ruta [BOL]','S','A');



--************************ COMANDOS PARA BOC ***********************************
insert into bodba.bo_comandos 
(id_comando,nombre_clase, descripcion, seguridad, activo)
values
(499,'ViewMonDespacho','Monitor de despacho [BOC]','S','A');

insert into bodba.bo_comandos 
(id_comando,nombre_clase, descripcion, seguridad, activo)
values
(500,'ViewDetRuta','Detalle de una ruta [BOC]','S','A');

insert into bodba.bo_comandos 
(id_comando,nombre_clase, descripcion, seguridad, activo)
values
(501,'ViewDetPedidoRuta','Detalle de un pedido ruta [BOC]','S','A');

insert into bodba.bo_comandos 
(id_comando,nombre_clase, descripcion, seguridad, activo)
values
(502,'ViewModDetPedidoRuta','Modificar un pedido de ruta [BOC]','S','A');

insert into bodba.bo_comandos 
(id_comando,nombre_clase, descripcion, seguridad, activo)
values
(503,'CambiaEstadoPedido','Cambia estado de pedido [BOC]','S','A');



--************************* PERMISOS PARA ADMINISTRADOR **********************************

-- BOC --

-- BOL --


INSERT INTO BODBA.BO_COMXPERF
(ID_COMANDO,ID_PERFIL) VALUES (480,1);

INSERT INTO BODBA.BO_COMXPERF
(ID_COMANDO,ID_PERFIL) VALUES (481,1);

INSERT INTO BODBA.BO_COMXPERF
(ID_COMANDO,ID_PERFIL) VALUES (482,1);

INSERT INTO BODBA.BO_COMXPERF
(ID_COMANDO,ID_PERFIL) VALUES (483,1);

INSERT INTO BODBA.BO_COMXPERF
(ID_COMANDO,ID_PERFIL) VALUES (484,1);

INSERT INTO BODBA.BO_COMXPERF
(ID_COMANDO,ID_PERFIL) VALUES (485,1);

INSERT INTO BODBA.BO_COMXPERF
(ID_COMANDO,ID_PERFIL) VALUES (486,1);

INSERT INTO BODBA.BO_COMXPERF
(ID_COMANDO,ID_PERFIL) VALUES (487,1);

INSERT INTO BODBA.BO_COMXPERF
(ID_COMANDO,ID_PERFIL) VALUES (488,1);

INSERT INTO BODBA.BO_COMXPERF
(ID_COMANDO,ID_PERFIL) VALUES (489,1);

INSERT INTO BODBA.BO_COMXPERF
(ID_COMANDO,ID_PERFIL) VALUES (490,1);

INSERT INTO BODBA.BO_COMXPERF
(ID_COMANDO,ID_PERFIL) VALUES (491,1);

INSERT INTO BODBA.BO_COMXPERF
(ID_COMANDO,ID_PERFIL) VALUES (492,1);

INSERT INTO BODBA.BO_COMXPERF
(ID_COMANDO,ID_PERFIL) VALUES (493,1);

INSERT INTO BODBA.BO_COMXPERF
(ID_COMANDO,ID_PERFIL) VALUES (494,1);

INSERT INTO BODBA.BO_COMXPERF
(ID_COMANDO,ID_PERFIL) VALUES (495,1);

INSERT INTO BODBA.BO_COMXPERF
(ID_COMANDO,ID_PERFIL) VALUES (496,1);

INSERT INTO BODBA.BO_COMXPERF
(ID_COMANDO,ID_PERFIL) VALUES (497,1);

INSERT INTO BODBA.BO_COMXPERF
(ID_COMANDO,ID_PERFIL) VALUES (498,1);

INSERT INTO BODBA.BO_COMXPERF
(ID_COMANDO,ID_PERFIL) VALUES (499,1);

INSERT INTO BODBA.BO_COMXPERF
(ID_COMANDO,ID_PERFIL) VALUES (500,1);

INSERT INTO BODBA.BO_COMXPERF
(ID_COMANDO,ID_PERFIL) VALUES (501,1);

INSERT INTO BODBA.BO_COMXPERF
(ID_COMANDO,ID_PERFIL) VALUES (502,1);

INSERT INTO BODBA.BO_COMXPERF
(ID_COMANDO,ID_PERFIL) VALUES (503,1);






