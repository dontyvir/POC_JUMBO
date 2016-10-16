drop table bodba.lista_negra_productos;
drop table bodba.lista_negra_subrubro;

create table bodba.lista_negra_subrubro(
	id integer not null GENERATED ALWAYS AS IDENTITY,
	subrubro char(6) not null,
	PRIMARY KEY  (id)
) in bo_tablespace;

create table bodba.lista_negra_productos(
	subrubro_id integer not null,
	producto_id integer not null, --de la tabla bo_productos
	cantidad_max DECIMAL(10,2), --mismo tipo que pro_inter_max de tabla fo_productos
	CONSTRAINT fk_lista_subrub_id FOREIGN KEY(subrubro_id) REFERENCES bodba.lista_negra_subrubro(id) ON DELETE CASCADE,
	CONSTRAINT fk_lista_pro_id FOREIGN KEY(producto_id) REFERENCES bodba.bo_productos(id_producto) ON DELETE CASCADE
) in bo_tablespace;

insert into bodba.BO_COMANDOS (ID_COMANDO, NOMBRE_CLASE, DESCRIPCION, SEGURIDAD, ACTIVO)
select max(id_comando)+1, 'ViewListaNegraProductos','ViewListaNegraProductos', 'S','A'  from bodba.bo_comandos;
insert into bodba.BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bodba.bo_comandos where  nombre_clase = 'ViewListaNegraProductos'), 121);
insert into bodba.BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bodba.bo_comandos where  nombre_clase = 'ViewListaNegraProductos'), 1);

insert into bodba.BO_COMANDOS (ID_COMANDO, NOMBRE_CLASE, DESCRIPCION, SEGURIDAD, ACTIVO)
select max(id_comando)+1, 'UpdSubrubros','UpdSubrubros', 'S','A'  from bodba.bo_comandos;
insert into bodba.BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bodba.bo_comandos where  nombre_clase = 'UpdSubrubros'), 121);
insert into bodba.BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bodba.bo_comandos where  nombre_clase = 'UpdSubrubros'), 1);

insert into bodba.BO_COMANDOS (ID_COMANDO, NOMBRE_CLASE, DESCRIPCION, SEGURIDAD, ACTIVO)
select max(id_comando)+1, 'ExportarSubrubrosListaNegra','ExportarSubrubrosListaNegra', 'S','A'  from bodba.bo_comandos;
insert into bodba.BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bodba.bo_comandos where  nombre_clase = 'ExportarSubrubrosListaNegra'), 121);
insert into bodba.BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bodba.bo_comandos where  nombre_clase = 'ExportarSubrubrosListaNegra'), 1);

