insert into bodba.BO_COMANDOS (ID_COMANDO, NOMBRE_CLASE, DESCRIPCION, SEGURIDAD, ACTIVO)
select max(id_comando)+1, 'ViewSustitutosNoReconocidos','ViewSustitutosNoReconocidos', 'S','A'  from bodba.bo_comandos;
insert into bodba.BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bodba.bo_comandos where  nombre_clase = 'ViewSustitutosNoReconocidos'), 121);
insert into bodba.BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bodba.bo_comandos where  nombre_clase = 'ViewSustitutosNoReconocidos'), 1);

insert into bodba.BO_COMANDOS (ID_COMANDO, NOMBRE_CLASE, DESCRIPCION, SEGURIDAD, ACTIVO)
select max(id_comando)+1, 'ViewSustitutosPedido','Buscar pedido', 'S','A'  from bodba.bo_comandos;
insert into bodba.BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bodba.bo_comandos where  nombre_clase = 'ViewSustitutosPedido'), 121);
insert into bodba.BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bodba.bo_comandos where  nombre_clase = 'ViewSustitutosPedido'), 1);


insert into bodba.BO_COMANDOS (ID_COMANDO, NOMBRE_CLASE, DESCRIPCION, SEGURIDAD, ACTIVO)
select max(id_comando)+1, 'ViewSustitutosBarra','Buscar producto por codigo de barra', 'S','A'  from bodba.bo_comandos;
insert into bodba.BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bodba.bo_comandos where  nombre_clase = 'ViewSustitutosBarra'), 121);
insert into bodba.BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bodba.bo_comandos where  nombre_clase = 'ViewSustitutosBarra'), 1);

insert into bodba.BO_COMANDOS (ID_COMANDO, NOMBRE_CLASE, DESCRIPCION, SEGURIDAD, ACTIVO)
select max(id_comando)+1, 'UpdSustituto','Actualiza sustituto no reconocido', 'S','A'  from bodba.bo_comandos;
insert into bodba.BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bodba.bo_comandos where  nombre_clase = 'UpdSustituto'), 121);
insert into bodba.BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bodba.bo_comandos where  nombre_clase = 'UpdSustituto'), 1);
