INSERT INTO BODBA.BO_ALERTAS (ID_ALERTA, NOMBRE, DESCRIPCION, TIPO, ORDEN, ACTIVO  )
VALUES (33, 'Susceptible a fraude', 'Primera Compra con Producto susceptible a fraude', 'I', 1, 'A');

INSERT INTO BODBA.BO_ALERTAS (ID_ALERTA, NOMBRE, DESCRIPCION, TIPO, ORDEN, ACTIVO  )
VALUES (34, 'Comuna Susc.a fraude', 'Comuna susceptible a fraude', 'I', 1, 'A');

                         
alter table bodba.bo_zonas add column MONTO_DESCUENTO_PC_CAT integer not null default 50000;

alter table bodba.bo_zonas add column MONTO_DESCUENTO_PC_TBK integer not null default 50000;

alter table bodba.bo_zonas add column MONTO_DESCUENTO_PC_PAR integer not null default 50000;



--nueva columna para alerta
alter table bo_comunas add column SUSCEPTIBLE_FRAUDE smallint not null default 0;
update bodba.bo_comunas set SUSCEPTIBLE_FRAUDE = 1 where id_comuna in (39,25,36,13,18,40);
insert into bodba.bo_parametros (id_parametro, nombre, valor) values(4, 'MONTO_VALIDA_PRIMERA_COMPRA', '80000');
insert into bodba.bo_parametros (id_parametro, nombre, valor) values(5, 'MONTO_LIMITE_OP', '300000')




--nuevo release
db2 connect to jmcl_ve
db2 -td#

DROP TRIGGER BODBA.U_BO_PEDIDOS_AFTER#

CREATE TRIGGER BODBA.U_BO_PEDIDOS_AFTER AFTER
UPDATE ON BODBA.BO_PEDIDOS 
REFERENCING NEW AS N 
FOR EACH ROW MODE DB2SQL 
BEGIN ATOMIC
        if N.id_estado = 10 then
            update fodba.fo_direcciones set DIR_FNUEVA = null where dir_id = N.dir_id;
        end if;
END#
