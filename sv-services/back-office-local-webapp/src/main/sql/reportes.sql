drop table bodba.encargados_pickeadores;
create table bodba.encargados_pickeadores (
  encargado_id int not null,
  pickeador_id int not null,
  local_id int not null,
  CONSTRAINT fk_enc_pick_usu1 FOREIGN KEY(encargado_id) REFERENCES bodba.bo_usuarios(id_usuario) ON DELETE CASCADE,
  CONSTRAINT fk_enc_pick_usu2 FOREIGN KEY(pickeador_id) REFERENCES bodba.bo_usuarios(id_usuario) ON DELETE CASCADE,
  CONSTRAINT fk_enc_pick_loc  FOREIGN KEY(local_id) REFERENCES bodba.bo_locales(id_local) ON DELETE CASCADE
)in bo_tablespace;


insert into bodba.BO_COMANDOS (ID_COMANDO, NOMBRE_CLASE, DESCRIPCION, SEGURIDAD, ACTIVO)
select max(id_comando)+1, 'EncargadoPickeadorView','Formulario para asignacion encargado pickeadores', 'S','A' from bodba.bo_comandos;
insert into bodba.BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bodba.bo_comandos where  nombre_clase = 'EncargadoPickeadorView'), 1);
insert into bodba.BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bodba.bo_comandos where  nombre_clase = 'EncargadoPickeadorView'), 63);



insert into bodba.BO_COMANDOS (ID_COMANDO, NOMBRE_CLASE, DESCRIPCION, SEGURIDAD, ACTIVO)
select max(id_comando)+1, 'ListaEncargadosView','Lista de nombres para el autocompletado', 'S','A' from bodba.bo_comandos;
insert into bodba.BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bodba.bo_comandos where  nombre_clase = 'ListaEncargadosView'), 1);
insert into bodba.BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bodba.bo_comandos where  nombre_clase = 'ListaEncargadosView'), 63);


insert into bodba.BO_COMANDOS (ID_COMANDO, NOMBRE_CLASE, DESCRIPCION, SEGURIDAD, ACTIVO)
select max(id_comando)+1, 'PickeadoresPorEncargadoView','Listado de pickeadores', 'S','A' from bodba.bo_comandos;
insert into bodba.BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bodba.bo_comandos where  nombre_clase = 'PickeadoresPorEncargadoView'), 1);
insert into bodba.BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bodba.bo_comandos where  nombre_clase = 'PickeadoresPorEncargadoView'), 63);


insert into bodba.BO_COMANDOS (ID_COMANDO, NOMBRE_CLASE, DESCRIPCION, SEGURIDAD, ACTIVO)
select max(id_comando)+1, 'PickeadoresPorEncargadoUpd','Actualiza asociacion pickeador encargado', 'S','A' from bodba.bo_comandos;
insert into bodba.BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bodba.bo_comandos where  nombre_clase = 'PickeadoresPorEncargadoUpd'), 1);
insert into bodba.BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bodba.bo_comandos where  nombre_clase = 'PickeadoresPorEncargadoUpd'), 63);


create table bodba.asistencias(
  id integer not null GENERATED ALWAYS AS IDENTITY,
  nombre varchar(2) not null,
  descripcion varchar(32) not null,
  orden int not null,
  PRIMARY KEY  (id),
  unique(nombre)
)in bo_tablespace;

insert into bodba.asistencias(nombre, descripcion, orden) values('A','Asiste a trabajar',1);
insert into bodba.asistencias(nombre, descripcion, orden) values('B','Bodeguero',2);
insert into bodba.asistencias(nombre, descripcion, orden) values('O','Otras labores',3);
insert into bodba.asistencias(nombre, descripcion, orden) values('D','Día libre',4);

insert into bodba.asistencias(nombre, descripcion, orden) values('CA','Ausente con aviso',5);
insert into bodba.asistencias(nombre, descripcion, orden) values('SA','Ausente sin aviso',6);
insert into bodba.asistencias(nombre, descripcion, orden) values('L','Licencia',7);
insert into bodba.asistencias(nombre, descripcion, orden) values('V','Vacaciones',8);

insert into bodba.asistencias(nombre, descripcion, orden) values('R','Renuncia',9);
insert into bodba.asistencias(nombre, descripcion, orden) values('DE','Desvinculación',10);
insert into bodba.asistencias(nombre, descripcion, orden) values('T','Traslado',11);
insert into bodba.asistencias(nombre, descripcion, orden) values('I','Nuevo Ingreso',12);


create table bodba.asistencias_pickeadores(
  id integer not null GENERATED ALWAYS AS IDENTITY,
  pickeador_id int not null,
  fecha date not null,
  asistencia_id int not null,
  local_id int not null,
  PRIMARY KEY  (id),
  CONSTRAINT fk_asi_pic_pick_id FOREIGN KEY(pickeador_id) REFERENCES bodba.bo_usuarios(id_usuario) ON DELETE CASCADE,
  CONSTRAINT fk_asi_pic_asis_id FOREIGN KEY(asistencia_id) REFERENCES bodba.asistencias(id) ON DELETE CASCADE,
  CONSTRAINT fk_asi_pic_loc_id  FOREIGN KEY(local_id) REFERENCES bodba.bo_locales(id_local) ON DELETE CASCADE,
  unique(pickeador_id, fecha)
)in bo_tablespace;


insert into bodba.BO_COMANDOS (ID_COMANDO, NOMBRE_CLASE, DESCRIPCION, SEGURIDAD, ACTIVO)
select max(id_comando)+1, 'SeguimientoDiarioView','SeguimientoDiarioView', 'S','A' from bodba.bo_comandos;
insert into bodba.BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bodba.bo_comandos where  nombre_clase = 'SeguimientoDiarioView'), 1);
insert into bodba.BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bodba.bo_comandos where  nombre_clase = 'SeguimientoDiarioView'), 63);



insert into bodba.BO_COMANDOS (ID_COMANDO, NOMBRE_CLASE, DESCRIPCION, SEGURIDAD, ACTIVO)
select max(id_comando)+1, 'SeguimientoPickeadoresView','SeguimientoPickeadoresView', 'S','A' from bodba.bo_comandos;
insert into bodba.BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bodba.bo_comandos where  nombre_clase = 'SeguimientoPickeadoresView'), 1);
insert into bodba.BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bodba.bo_comandos where  nombre_clase = 'SeguimientoPickeadoresView'), 63);


insert into bodba.BO_COMANDOS (ID_COMANDO, NOMBRE_CLASE, DESCRIPCION, SEGURIDAD, ACTIVO)
select max(id_comando)+1, 'GrabarAsistencia','GrabarAsistencia', 'S','A' from bodba.bo_comandos;
insert into bodba.BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bodba.bo_comandos where  nombre_clase = 'GrabarAsistencia'), 1);
insert into bodba.BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bodba.bo_comandos where  nombre_clase = 'GrabarAsistencia'), 63);
