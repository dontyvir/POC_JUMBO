create table bodba.destacados(
    id integer not null GENERATED ALWAYS AS IDENTITY,
    descripcion varchar(128) not null,
    fecha_hora_ini timestamp not null,
    fecha_hora_fin timestamp not null,
    imagen varchar(128) not null,
    PRIMARY KEY  (id)
) in bo_tablespace

create table bodba.destacados_productos(
	destacado_id integer not null,
	producto_id integer not null,
	
	CONSTRAINT fk_des_pro_des_id FOREIGN KEY(destacado_id) REFERENCES destacados(id) ON DELETE CASCADE,
	CONSTRAINT fk_des_pro_pro_id FOREIGN KEY(producto_id) REFERENCES bo_productos(id_producto) ON DELETE CASCADE 
)in bo_tablespace

create table bodba.destacados_locales(
	local_id integer not null,
	destacado_id integer not null,
	CONSTRAINT fk_des_loc_loc_id FOREIGN KEY(local_id) REFERENCES bo_locales(id_local) ON DELETE CASCADE,
	CONSTRAINT fk_des_loc_des_id FOREIGN KEY(destacado_id) REFERENCES destacados(id) ON DELETE CASCADE
)in bo_tablespace


insert into BO_COMANDOS (ID_COMANDO, NOMBRE_CLASE, DESCRIPCION, SEGURIDAD, ACTIVO)
select max(id_comando)+1, 'ViewMonDestacados','Lista de destacados', 'S','A'  from bo_comandos
insert into BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bo_comandos where  nombre_clase = 'ViewMonDestacados'), 1)


insert into BO_COMANDOS (ID_COMANDO, NOMBRE_CLASE, DESCRIPCION, SEGURIDAD, ACTIVO)
select max(id_comando)+1, 'ViewDestacadoNewForm','Formulario para nuevo destacado', 'S','A'  from bo_comandos
insert into BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bo_comandos where  nombre_clase = 'ViewDestacadoNewForm'), 1)

insert into BO_COMANDOS (ID_COMANDO, NOMBRE_CLASE, DESCRIPCION, SEGURIDAD, ACTIVO)
select max(id_comando)+1, 'AddDestacado','Grabar destacado', 'S','A'  from bo_comandos
insert into BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bo_comandos where  nombre_clase = 'AddDestacado'), 1)


insert into BO_COMANDOS (ID_COMANDO, NOMBRE_CLASE, DESCRIPCION, SEGURIDAD, ACTIVO)
select max(id_comando)+1, 'DelDestacado','Borra destacado y sus dependencias', 'S','A'  from bo_comandos
insert into BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bo_comandos where  nombre_clase = 'DelDestacado'), 1)

insert into BO_COMANDOS (ID_COMANDO, NOMBRE_CLASE, DESCRIPCION, SEGURIDAD, ACTIVO)
select max(id_comando)+1, 'UpdDestacado','Modifica destacado y sus locales y productos', 'S','A'  from bo_comandos
insert into BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bo_comandos where  nombre_clase = 'UpdDestacado'), 1)


insert into BO_COMANDOS (ID_COMANDO, NOMBRE_CLASE, DESCRIPCION, SEGURIDAD, ACTIVO)
select max(id_comando)+1, 'ViewDestacado','Muestra los datos del detacado', 'S','A'  from bo_comandos
insert into BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bo_comandos where  nombre_clase = 'ViewDestacado'), 1)


insert into BO_COMANDOS (ID_COMANDO, NOMBRE_CLASE, DESCRIPCION, SEGURIDAD, ACTIVO)
select max(id_comando)+1, 'ViewDestacadoUpdForm','Para formulario de modificación', 'S','A'  from bo_comandos
insert into BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bo_comandos where  nombre_clase = 'ViewDestacadoUpdForm'), 1)






--Modificación FO

ALTER TABLE bodba.destacados_productos DROP CONSTRAINT fk_des_pro_pro_id

create table bodba.destacados_productos_old like bodba.destacados_productos 
insert into bodba.destacados_productos_old  select * from bodba.destacados_productos

merge into bodba.destacados_productos as des
using (select pro_id, pro_id_bo from fodba.fo_productos) as fopro
on (fopro.pro_id_bo = des.producto_id)
WHEN MATCHED THEN
     UPDATE SET des.producto_id = fopro.pro_id 
     
     
select * from bodba.destacados_productos where producto_id not in (select pro_id from fodba.fo_productos)
delete from bodba.destacados_productos where producto_id not in (select pro_id from fodba.fo_productos)


ALTER TABLE bodba.destacados_productos ADD CONSTRAINT fk_des_pro_pro_id FOREIGN KEY(producto_id) REFERENCES fodba.fo_productos(pro_id) ON DELETE CASCADE 
