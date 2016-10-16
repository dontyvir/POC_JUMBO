insert into bodba.BO_COMANDOS (ID_COMANDO, NOMBRE_CLASE, DESCRIPCION, SEGURIDAD, ACTIVO)
select max(id_comando)+1, 'PreciosIndex','PreciosIndex', 'S','A' from bodba.bo_comandos;
insert into bodba.BO_COMXPERF(ID_COMANDO, ID_PERFIL)
values ( (select id_comando from bodba.bo_comandos where  nombre_clase = 'PreciosIndex'), 1);

insert into bodba.BO_COMANDOS (ID_COMANDO, NOMBRE_CLASE, DESCRIPCION, SEGURIDAD, ACTIVO)
select max(id_comando)+1, 'PreciosEdit','PreciosEdit', 'S','A' from bodba.bo_comandos;
insert into bodba.BO_COMXPERF(ID_COMANDO, ID_PERFIL)
values ( (select id_comando from bodba.bo_comandos where  nombre_clase = 'PreciosEdit'), 1);

insert into bodba.BO_COMANDOS (ID_COMANDO, NOMBRE_CLASE, DESCRIPCION, SEGURIDAD, ACTIVO)
select max(id_comando)+1, 'PreciosUpdate','PreciosUpdate', 'S','A' from bodba.bo_comandos;
insert into bodba.BO_COMXPERF(ID_COMANDO, ID_PERFIL)
values ( (select id_comando from bodba.bo_comandos where  nombre_clase = 'PreciosUpdate'), 1);
