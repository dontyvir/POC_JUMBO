insert into bodba.BO_COMANDOS (ID_COMANDO, NOMBRE_CLASE, DESCRIPCION, SEGURIDAD, ACTIVO)
select max(id_comando)+1, 'CuadraturaTransporteView','Formulario para genera informes', 'S','A' from bodba.bo_comandos;
insert into bodba.BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bodba.bo_comandos where  nombre_clase = 'CuadraturaTransporteView'), 1);
insert into bodba.BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bodba.bo_comandos where  nombre_clase = 'CuadraturaTransporteView'), 6);
insert into bodba.BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bodba.bo_comandos where  nombre_clase = 'CuadraturaTransporteView'), 204);


insert into bodba.BO_COMANDOS (ID_COMANDO, NOMBRE_CLASE, DESCRIPCION, SEGURIDAD, ACTIVO)
select max(id_comando)+1, 'CuadraturaTransporteGenerar','Generar informe', 'S','A' from bodba.bo_comandos;
insert into bodba.BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bodba.bo_comandos where  nombre_clase = 'CuadraturaTransporteGenerar'), 1);
insert into bodba.BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bodba.bo_comandos where  nombre_clase = 'CuadraturaTransporteGenerar'), 6);
insert into bodba.BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bodba.bo_comandos where  nombre_clase = 'CuadraturaTransporteGenerar'), 204);

