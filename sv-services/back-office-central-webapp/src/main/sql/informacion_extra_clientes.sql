drop table fodba.fo_clientes_datos;
drop table fodba.fo_clientes_alternativas;
drop table fodba.fo_clientes_preguntas;

create table fodba.fo_clientes_preguntas(
    id integer not null GENERATED ALWAYS AS IDENTITY,
    enunciado varchar(64) not null,
    control varchar(32) not null, --combobox, fecha, numero, checkbox
    orden int not null,
    depende_alt_id int, --depende si se ha elegido la alternativa en preguntas anteriores
    PRIMARY KEY  (id)
) in fo_tablespace;

create table fodba.fo_clientes_alternativas(
    id integer not null GENERATED ALWAYS AS IDENTITY,
    pregunta_id integer not null,
    enunciado varchar(64) not null,
    orden int not null,
    PRIMARY KEY  (id),
    CONSTRAINT fk_cli_alt_pre_id FOREIGN KEY(pregunta_id) REFERENCES fodba.fo_clientes_preguntas(id) ON DELETE CASCADE
) in fo_tablespace;

create table fodba.fo_clientes_datos(
    id integer not null GENERATED ALWAYS AS IDENTITY,
    cliente_id integer not null,
    pregunta_id integer not null,
    alternativa_id integer,  --permite null
    respuesta int, --permite null
    PRIMARY KEY  (id),
    CONSTRAINT fk_cli_dat_cli_id FOREIGN KEY(cliente_id) REFERENCES fodba.fo_clientes(cli_id) ON DELETE CASCADE,
    CONSTRAINT fk_cli_dat_pre_id FOREIGN KEY(pregunta_id) REFERENCES fodba.fo_clientes_preguntas(id) ON DELETE CASCADE,
    CONSTRAINT fk_cli_dat_alt_id FOREIGN KEY(alternativa_id) REFERENCES fodba.fo_clientes_alternativas(id) ON DELETE CASCADE
) in fo_tablespace;



insert into fodba.fo_clientes_preguntas(orden, enunciado, control)
values(1, 'Estado Civil', 'combobox');
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(1,'Soltero/a', (select id from fodba.fo_clientes_preguntas where enunciado = 'Estado Civil'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(2,'Casado/a', (select id from fodba.fo_clientes_preguntas where enunciado = 'Estado Civil'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(3,'Divorciado/a', (select id from fodba.fo_clientes_preguntas where enunciado = 'Estado Civil'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(4,'Viudo/a', (select id from fodba.fo_clientes_preguntas where enunciado = 'Estado Civil'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(5,'Otro', (select id from fodba.fo_clientes_preguntas where enunciado = 'Estado Civil'));


insert into fodba.fo_clientes_preguntas(orden, enunciado, control, depende_alt_id)
values(2, 'Fecha de Aniversario de Matrimonio', 'fecha', (select id from fodba.fo_clientes_alternativas where enunciado = 'Casado/a'));


insert into fodba.fo_clientes_preguntas(orden, enunciado, control)
values(3, 'Nivel de Estudios', 'combobox');
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(1,'Sin estudios', (select id from fodba.fo_clientes_preguntas where enunciado = 'Nivel de Estudios'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(2,'Básica', (select id from fodba.fo_clientes_preguntas where enunciado = 'Nivel de Estudios'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(3,'Media', (select id from fodba.fo_clientes_preguntas where enunciado = 'Nivel de Estudios'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(4,'Técnica', (select id from fodba.fo_clientes_preguntas where enunciado = 'Nivel de Estudios'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(5,'Universitaria Incompleta', (select id from fodba.fo_clientes_preguntas where enunciado = 'Nivel de Estudios'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(6,'Universitaria Completa', (select id from fodba.fo_clientes_preguntas where enunciado = 'Nivel de Estudios'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(7,'Postgrado Incompleto', (select id from fodba.fo_clientes_preguntas where enunciado = 'Nivel de Estudios'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(8,'Postgrado Completo', (select id from fodba.fo_clientes_preguntas where enunciado = 'Nivel de Estudios'));


insert into fodba.fo_clientes_preguntas(orden, enunciado, control)
values(4, 'Ocupación', 'combobox');
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(1,'Asistente', (select id from fodba.fo_clientes_preguntas where enunciado = 'Ocupación'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(2,'Analista/Asesor', (select id from fodba.fo_clientes_preguntas where enunciado = 'Ocupación'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(3,'Ejecutivo Junior', (select id from fodba.fo_clientes_preguntas where enunciado = 'Ocupación'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(4,'Ejecutivo Senior', (select id from fodba.fo_clientes_preguntas where enunciado = 'Ocupación'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(5,'Supervisor', (select id from fodba.fo_clientes_preguntas where enunciado = 'Ocupación'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(6,'Jefe de Área', (select id from fodba.fo_clientes_preguntas where enunciado = 'Ocupación'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(7,'Subgerente de Área', (select id from fodba.fo_clientes_preguntas where enunciado = 'Ocupación'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(8,'Gerente de Área', (select id from fodba.fo_clientes_preguntas where enunciado = 'Ocupación'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(9,'Gerente de División', (select id from fodba.fo_clientes_preguntas where enunciado = 'Ocupación'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(10,'Subgerente General', (select id from fodba.fo_clientes_preguntas where enunciado = 'Ocupación'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(11,'Gerente General', (select id from fodba.fo_clientes_preguntas where enunciado = 'Ocupación'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(12,'Miembro del Directorio', (select id from fodba.fo_clientes_preguntas where enunciado = 'Ocupación'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(13,'Dueño/a de Casa', (select id from fodba.fo_clientes_preguntas where enunciado = 'Ocupación'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(14,'Otro Profesional', (select id from fodba.fo_clientes_preguntas where enunciado = 'Ocupación'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(15,'Otro no Profesional', (select id from fodba.fo_clientes_preguntas where enunciado = 'Ocupación'));


insert into fodba.fo_clientes_preguntas(orden, enunciado, control)
values(5, 'Principal Sostenedor', 'combobox');
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(1,'Yo', (select id from fodba.fo_clientes_preguntas where enunciado = 'Principal Sostenedor'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(2,'Cónyuge', (select id from fodba.fo_clientes_preguntas where enunciado = 'Principal Sostenedor'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(3,'Padre', (select id from fodba.fo_clientes_preguntas where enunciado = 'Principal Sostenedor'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(4,'Madre', (select id from fodba.fo_clientes_preguntas where enunciado = 'Principal Sostenedor'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(5,'Hijo/a', (select id from fodba.fo_clientes_preguntas where enunciado = 'Principal Sostenedor'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(6,'Otro', (select id from fodba.fo_clientes_preguntas where enunciado = 'Principal Sostenedor'));


insert into fodba.fo_clientes_preguntas(orden, enunciado, control)
values(6, 'N° de personas que viven en su hogar', 'numero');

insert into fodba.fo_clientes_preguntas(orden, enunciado, control)
values(7, 'N° de personas entre 0 y 5 años', 'numero');

insert into fodba.fo_clientes_preguntas(orden, enunciado, control)
values(8, 'N° de personas entre 6 y 17 años', 'numero');

insert into fodba.fo_clientes_preguntas(orden, enunciado, control)
values(9, 'N° de personas entre 18  y 26 años', 'numero');

insert into fodba.fo_clientes_preguntas(orden, enunciado, control)
values(10, 'N° de vehículos que tiene en su hogar', 'numero');


insert into fodba.fo_clientes_preguntas(orden, enunciado, control)
values(11, 'Mascotas', 'checkbox');
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(1,'Perro', (select id from fodba.fo_clientes_preguntas where enunciado = 'Mascotas'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(2,'Gato', (select id from fodba.fo_clientes_preguntas where enunciado = 'Mascotas'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(3,'Hámster o similar', (select id from fodba.fo_clientes_preguntas where enunciado = 'Mascotas'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(4,'Conejo', (select id from fodba.fo_clientes_preguntas where enunciado = 'Mascotas'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(5,'Aves', (select id from fodba.fo_clientes_preguntas where enunciado = 'Mascotas'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(6,'Tortuga', (select id from fodba.fo_clientes_preguntas where enunciado = 'Mascotas'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(7,'Peces', (select id from fodba.fo_clientes_preguntas where enunciado = 'Mascotas'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(8,'Otros', (select id from fodba.fo_clientes_preguntas where enunciado = 'Mascotas'));


insert into fodba.fo_clientes_preguntas(orden, enunciado, control)
values(12, 'Ingreso familiar', 'combobox');
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(1,'Menos de $ 1.100.000', (select id from fodba.fo_clientes_preguntas where enunciado = 'Ingreso familiar'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(2,'Entre $ 1.100.001 y $ 1.800.000', (select id from fodba.fo_clientes_preguntas where enunciado = 'Ingreso familiar'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(3,'Entre $ 1.800.001 y $ 2.800.000', (select id from fodba.fo_clientes_preguntas where enunciado = 'Ingreso familiar'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(4,'Entre $ 2.800.001 y $ 7.500.000', (select id from fodba.fo_clientes_preguntas where enunciado = 'Ingreso familiar'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(5,'Más de $ 7.500.000', (select id from fodba.fo_clientes_preguntas where enunciado = 'Ingreso familiar'));


insert into fodba.fo_clientes_preguntas(orden, enunciado, control)
values(13, 'Gasto mensual en supermercado', 'combobox');
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(1,'Menos de $ 100.000', (select id from fodba.fo_clientes_preguntas where enunciado = 'Gasto mensual en supermercado'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(2,'Entre $ 100.001 y $ 250.000', (select id from fodba.fo_clientes_preguntas where enunciado = 'Gasto mensual en supermercado'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(3,'Entre $ 250.001 y $ 500.001', (select id from fodba.fo_clientes_preguntas where enunciado = 'Gasto mensual en supermercado'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(4,'Entre $ 500.000 y $ 1.000.000', (select id from fodba.fo_clientes_preguntas where enunciado = 'Gasto mensual en supermercado'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(5,'Más de $ 1.000.001', (select id from fodba.fo_clientes_preguntas where enunciado = 'Gasto mensual en supermercado'));


insert into fodba.fo_clientes_preguntas(orden, enunciado, control)
values(14, 'Actualmente, cuánto de ese gasto es una compra vía Internet', 'combobox');
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(1,'Menos de 25%', (select id from fodba.fo_clientes_preguntas where enunciado = 'Actualmente, cuánto de ese gasto es una compra vía Internet'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(2,'Entre 26% y 50%', (select id from fodba.fo_clientes_preguntas where enunciado = 'Actualmente, cuánto de ese gasto es una compra vía Internet'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(3,'Entre 51% y 75%', (select id from fodba.fo_clientes_preguntas where enunciado = 'Actualmente, cuánto de ese gasto es una compra vía Internet'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(4,'Más de 75%', (select id from fodba.fo_clientes_preguntas where enunciado = 'Actualmente, cuánto de ese gasto es una compra vía Internet'));


insert into fodba.fo_clientes_preguntas(orden, enunciado, control)
values(15, 'N° de veces que compra mercadería al mes', 'numero');

insert into fodba.fo_clientes_preguntas(orden, enunciado, control)
values(16, 'Actualmente, cuántas de esas veces son compras vía Internet', 'numero');




/*
insert into fodba.fo_clientes_preguntas(orden, enunciado, control)
values(17, 'Lugares más frecuentes donde veranea', 'combobox');
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(1,'Extranjero', (select id from fodba.fo_clientes_preguntas where enunciado = 'Lugares más frecuentes donde veranea'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(2,'I región', (select id from fodba.fo_clientes_preguntas where enunciado = 'Lugares más frecuentes donde veranea'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(3,'II región', (select id from fodba.fo_clientes_preguntas where enunciado = 'Lugares más frecuentes donde veranea'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(4,'III región', (select id from fodba.fo_clientes_preguntas where enunciado = 'Lugares más frecuentes donde veranea'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(5,'IV región', (select id from fodba.fo_clientes_preguntas where enunciado = 'Lugares más frecuentes donde veranea'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(6,'V región', (select id from fodba.fo_clientes_preguntas where enunciado = 'Lugares más frecuentes donde veranea'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(7,'VI región', (select id from fodba.fo_clientes_preguntas where enunciado = 'Lugares más frecuentes donde veranea'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(8,'VII región', (select id from fodba.fo_clientes_preguntas where enunciado = 'Lugares más frecuentes donde veranea'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(9,'VIII región', (select id from fodba.fo_clientes_preguntas where enunciado = 'Lugares más frecuentes donde veranea'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(10,'IX región', (select id from fodba.fo_clientes_preguntas where enunciado = 'Lugares más frecuentes donde veranea'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(11,'X región', (select id from fodba.fo_clientes_preguntas where enunciado = 'Lugares más frecuentes donde veranea'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(12,'XI región', (select id from fodba.fo_clientes_preguntas where enunciado = 'Lugares más frecuentes donde veranea'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(13,'XII región', (select id from fodba.fo_clientes_preguntas where enunciado = 'Lugares más frecuentes donde veranea'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(14,'XIV región', (select id from fodba.fo_clientes_preguntas where enunciado = 'Lugares más frecuentes donde veranea'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(15,'XV región', (select id from fodba.fo_clientes_preguntas where enunciado = 'Lugares más frecuentes donde veranea'));
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID)
VALUES(16,'Región Metropolitana', (select id from fodba.fo_clientes_preguntas where enunciado = 'Lugares más frecuentes donde veranea'));


insert into fodba.fo_clientes_preguntas(orden, enunciado, control, depende_alt_id)
values(17, 'Comunas', 'combobox', (select id from fodba.fo_clientes_alternativas where enunciado = 'XV región'));
insert into fodba.fo_clientes_preguntas(orden, enunciado, control, depende_alt_id)
values(17, 'Comunas', 'combobox', (select id from fodba.fo_clientes_alternativas where enunciado = 'I región'));
insert into fodba.fo_clientes_preguntas(orden, enunciado, control, depende_alt_id)
values(17, 'Comunas', 'combobox', (select id from fodba.fo_clientes_alternativas where enunciado = 'II región'));
insert into fodba.fo_clientes_preguntas(orden, enunciado, control, depende_alt_id)
values(17, 'Comunas', 'combobox', (select id from fodba.fo_clientes_alternativas where enunciado = 'III región'));
insert into fodba.fo_clientes_preguntas(orden, enunciado, control, depende_alt_id)
values(17, 'Comunas', 'combobox', (select id from fodba.fo_clientes_alternativas where enunciado = 'IV región'));
insert into fodba.fo_clientes_preguntas(orden, enunciado, control, depende_alt_id)
values(17, 'Comunas', 'combobox', (select id from fodba.fo_clientes_alternativas where enunciado = 'V región'));
insert into fodba.fo_clientes_preguntas(orden, enunciado, control, depende_alt_id)
values(17, 'Comunas', 'combobox', (select id from fodba.fo_clientes_alternativas where enunciado = 'VI región'));
insert into fodba.fo_clientes_preguntas(orden, enunciado, control, depende_alt_id)
values(17, 'Comunas', 'combobox', (select id from fodba.fo_clientes_alternativas where enunciado = 'VII región'));
insert into fodba.fo_clientes_preguntas(orden, enunciado, control, depende_alt_id)
values(17, 'Comunas', 'combobox', (select id from fodba.fo_clientes_alternativas where enunciado = 'VIII región'));
insert into fodba.fo_clientes_preguntas(orden, enunciado, control, depende_alt_id)
values(17, 'Comunas', 'combobox', (select id from fodba.fo_clientes_alternativas where enunciado = 'IX región'));
insert into fodba.fo_clientes_preguntas(orden, enunciado, control, depende_alt_id)
values(17, 'Comunas', 'combobox', (select id from fodba.fo_clientes_alternativas where enunciado = 'XIV región'));
insert into fodba.fo_clientes_preguntas(orden, enunciado, control, depende_alt_id)
values(17, 'Comunas', 'combobox', (select id from fodba.fo_clientes_alternativas where enunciado = 'X región'));
insert into fodba.fo_clientes_preguntas(orden, enunciado, control, depende_alt_id)
values(17, 'Comunas', 'combobox', (select id from fodba.fo_clientes_alternativas where enunciado = 'XI región'));
insert into fodba.fo_clientes_preguntas(orden, enunciado, control, depende_alt_id)
values(17, 'Comunas', 'combobox', (select id from fodba.fo_clientes_alternativas where enunciado = 'XII región'));
insert into fodba.fo_clientes_preguntas(orden, enunciado, control, depende_alt_id)
values(17, 'Comunas', 'combobox', (select id from fodba.fo_clientes_alternativas where enunciado = 'Región Metropolitana'));



INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(1,'Arica',18);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(2,'Camarones',18);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(3,'Putre',18);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(4,'General Lagos',18);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(5,'Alto Hospicio',19);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(6,'Iquique',19);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(7,'Huara',19);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(8,'Camiña',19);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(9,'Colchane',19);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(10,'Pica',19);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(11,'Pozo Almonte',19);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(12,'Tocopilla',20);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(13,'María Elena',20);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(14,'Calama',20);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(15,'Ollagüe',20);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(16,'San Pedro de Atacama',20);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(17,'Antofagasta',20);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(18,'Mejillones',20);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(19,'Sierra Gorda',20);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(20,'Taltal',20);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(21,'Chañaral',21);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(22,'Diego de Almagro',21);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(23,'Copiapó',21);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(24,'Caldera',21);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(25,'Tierra Amarilla',21);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(26,'Vallenar',21);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(27,'Freirina',21);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(28,'Huasco',21);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(29,'Alto del Carmen',21);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(30,'La Serena',22);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(31,'La Higuera',22);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(32,'Coquimbo',22);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(33,'Andacollo',22);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(34,'Vicuña',22);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(35,'Paihuano',22);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(36,'Ovalle',22);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(37,'Río Hurtado',22);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(38,'Monte Patria',22);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(39,'Combarbalá',22);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(40,'Punitaqui',22);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(41,'Illapel',22);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(42,'Salamanca',22);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(43,'Los Vilos',22);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(44,'Canela',22);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(45,'La Ligua',23);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(46,'Petorca',23);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(47,'Cabildo',23);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(48,'Zapallar',23);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(49,'Papudo',23);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(50,'Los Andes',23);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(51,'San Esteban',23);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(52,'Calle Larga',23);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(53,'Rinconada',23);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(54,'San Felipe',23);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(55,'Putaendo',23);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(56,'Santa María',23);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(57,'Panquehue',23);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(58,'Llayllay',23);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(59,'Catemu',23);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(60,'Quillota',23);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(61,'La Cruz',23);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(62,'Calera',23);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(63,'Nogales',23);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(64,'Hijuelas',23);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(65,'Limache',23);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(66,'Olmué',23);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(67,'Valparaíso',23);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(68,'Viña del Mar',23);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(69,'Quintero',23);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(70,'Puchuncaví',23);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(71,'Quilpué',23);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(72,'Villa Alemana',23);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(73,'Casablanca',23);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(74,'Concón',23);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(75,'Juan Fernández',23);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(76,'San Antonio',23);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(77,'Cartagena',23);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(78,'El Tabo',23);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(79,'El Quisco',23);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(80,'Algarrobo',23);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(81,'Santo Domingo',23);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(82,'Isla de Pascua',23);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(83,'Rancagua',24);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(84,'Graneros',24);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(85,'Mostazal',24);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(86,'Codegua',24);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(87,'Machalí',24);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(88,'Olivar',24);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(89,'Requinoa',24);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(90,'Rengo',24);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(91,'Malloa',24);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(92,'Quinta de Tilcoco',24);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(93,'San Vicente',24);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(94,'Pichidegua',24);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(95,'Peumo',24);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(96,'Coltauco',24);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(97,'Coinco',24);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(98,'Doñihue',24);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(99,'Las Cabras',24);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(100,'San Fernando',24);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(101,'Chimbarongo',24);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(102,'Placilla',24);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(103,'Nancagua',24);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(104,'Chépica',24);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(105,'Santa Cruz',24);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(106,'Lolol',24);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(107,'Pumanque',24);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(108,'Palmilla',24);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(109,'Peralillo',24);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(110,'Pichilemu',24);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(111,'Navidad',24);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(112,'Litueche',24);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(113,'La Estrella',24);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(114,'Marchihue',24);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(115,'Paredones',24);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(116,'Curicó',25);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(117,'Teno',25);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(118,'Romeral',25);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(119,'Molina',25);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(120,'Sagrada Familia',25);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(121,'Hualañé',25);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(122,'Licantén',25);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(123,'Vichuquén',25);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(124,'Rauco',25);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(125,'Talca',25);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(126,'Pelarco',25);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(127,'Río Claro',25);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(128,'San Clemente',25);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(129,'Maule',25);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(130,'San Rafael',25);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(131,'Empedrado',25);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(132,'Pencahue',25);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(133,'Constitución',25);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(134,'Curepto',25);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(135,'Linares',25);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(136,'Yerbas Buenas',25);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(137,'Colbún',25);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(138,'Longaví',25);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(139,'Parral',25);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(140,'Retiro',25);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(141,'Villa Alegre',25);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(142,'San Javier',25);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(143,'Cauquenes',25);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(144,'Pelluhue',25);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(145,'Chanco',25);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(146,'Chillán',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(147,'San Carlos',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(148,'Ñiquén',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(149,'San Fabián',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(150,'Coihueco',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(151,'Pinto',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(152,'San Ignacio',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(153,'El Carmen',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(154,'Yungay',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(155,'Pemuco',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(156,'Bulnes',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(157,'Quillón',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(158,'Ránquil',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(159,'Portezuelo',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(160,'Coelemu',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(161,'Treguaco',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(162,'Cobquecura',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(163,'Quirihue',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(164,'Ninhue',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(165,'San Nicolás',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(166,'Chillán Viejo',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(167,'Alto Biobío',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(168,'Los Angeles',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(169,'Cabrero',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(170,'Tucapel',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(171,'Antuco',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(172,'Quilleco',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(173,'Santa Bárbara',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(174,'Quilaco',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(175,'Mulchén',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(176,'Negrete',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(177,'Nacimiento',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(178,'Laja',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(179,'San Rosendo',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(180,'Yumbel',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(181,'Concepción',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(182,'Talcahuano',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(183,'Penco',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(184,'Tomé',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(185,'Florida',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(186,'Hualpén',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(187,'Hualqui',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(188,'Santa Juana',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(189,'Lota',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(190,'Coronel',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(191,'San Pedro de la Paz',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(192,'Chiguayante',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(193,'Lebu',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(194,'Arauco',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(195,'Curanilahue',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(196,'Los Alamos',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(197,'Cañete',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(198,'Contulmo',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(199,'Tirua',26);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(200,'Angol',27);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(201,'Renaico',27);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(202,'Collipulli',27);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(203,'Lonquimay',27);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(204,'Curacautín',27);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(205,'Ercilla',27);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(206,'Victoria',27);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(207,'Traiguén',27);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(208,'Lumaco',27);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(209,'Purén',27);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(210,'Los Sauces',27);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(211,'Temuco',27);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(212,'Lautaro',27);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(213,'Perquenco',27);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(214,'Vilcún',27);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(215,'Cholchol',27);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(216,'Cunco',27);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(217,'Melipeuco',27);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(218,'Curarrehue',27);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(219,'Pucón',27);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(220,'Villarrica',27);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(221,'Freire',27);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(222,'Pitrufquén',27);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(223,'Gorbea',27);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(224,'Loncoche',27);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(225,'Toltén',27);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(226,'Teodoro Schmidt',27);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(227,'Saavedra',27);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(228,'Carahue',27);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(229,'Nueva Imperial',27);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(230,'Galvarino',27);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(231,'Padre las Casas',27);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(232,'Valdivia',28);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(233,'Mariquina',28);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(234,'Lanco',28);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(235,'Máfil',28);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(236,'Corral',28);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(237,'Los Lagos',28);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(238,'Panguipulli',28);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(239,'Paillaco',28);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(240,'La Unión',28);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(241,'Futrono',28);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(242,'Río Bueno',28);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(243,'Lago Ranco',28);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(244,'Osorno',29);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(245,'San Pablo',29);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(246,'Puyehue',29);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(247,'Puerto Octay',29);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(248,'Purranque',29);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(249,'Río Negro',29);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(250,'San Juan de la Costa',29);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(251,'Puerto Montt',29);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(252,'Puerto Varas',29);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(253,'Cochamó',29);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(254,'Calbuco',29);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(255,'Maullín',29);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(256,'Los Muermos',29);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(257,'Fresia',29);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(258,'Llanquihue',29);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(259,'Frutillar',29);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(260,'Castro',29);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(261,'Ancud',29);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(262,'Quemchi',29);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(263,'Dalcahue',29);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(264,'Curaco de Vélez',29);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(265,'Quinchao',29);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(266,'Puqueldón',29);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(267,'Chonchi',29);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(268,'Queilén',29);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(269,'Quellón',29);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(270,'Chaitén',29);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(271,'Hualaihué',29);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(272,'Futaleufú',29);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(273,'Palena',29);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(274,'Coihaique',30);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(275,'Lago Verde',30);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(276,'Aisén',30);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(277,'Cisnes',30);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(278,'Guaitecas',30);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(279,'Chile Chico',30);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(280,'Río Ibánez',30);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(281,'Cochrane',30);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(282,'O''Higgins',30);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(283,'Tortel',30);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(284,'Natales',31);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(285,'Torres del Paine',31);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(286,'Punta Arenas',31);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(287,'Rio Verde',31);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(288,'Laguna Blanca',31);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(289,'San Gregorio',31);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(290,'Porvenir',31);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(291,'Primavera',31);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(292,'Timaukel',31);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(293,'Cabo de Hornos',31);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(294,'Antártica',31);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(295,'Santiago',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(296,'Independencia',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(297,'Conchalí',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(298,'Huechuraba',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(299,'Recoleta',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(300,'Providencia',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(301,'Vitacura',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(302,'Lo Barnechea',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(303,'Las Condes',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(304,'Ñuñoa',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(305,'La Reina',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(306,'Macul',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(307,'Peñalolén',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(308,'La Florida',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(309,'San Joaquín',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(310,'La Granja',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(311,'La Pintana',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(312,'San Ramón',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(313,'San Miguel',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(314,'La Cisterna',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(315,'El Bosque',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(316,'Pedro Aguirre Cerda',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(317,'Lo Espejo',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(318,'Estación Central',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(319,'Cerrillos',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(320,'Maipú',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(321,'Quinta Normal',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(322,'Lo Prado',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(323,'Pudahuel',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(324,'Cerro Navia',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(325,'Renca',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(326,'Quilicura',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(327,'Colina',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(328,'Lampa',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(329,'Tiltil',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(330,'Puente Alto',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(331,'San José de Maipo',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(332,'Pirque',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(333,'San Bernardo',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(334,'Buin',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(335,'Paine',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(336,'Calera de Tango',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(337,'Melipilla',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(338,'María Pinto',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(339,'Curacaví',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(340,'Alhué',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(341,'San Pedro',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(342,'Talagante',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(343,'Peñaflor',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(344,'Isla de Maipo',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(345,'El Monte',32);
INSERT INTO FODBA.FO_CLIENTES_ALTERNATIVAS(ORDEN, ENUNCIADO, PREGUNTA_ID) VALUES(346,'Padre Hurtado',32);
*/