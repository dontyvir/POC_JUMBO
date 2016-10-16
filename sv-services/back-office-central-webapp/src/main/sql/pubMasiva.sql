insert into bodba.BO_COMANDOS (ID_COMANDO, NOMBRE_CLASE, DESCRIPCION, SEGURIDAD, ACTIVO)
select max(id_comando)+1, 'ViewPubMasivaForm','Formulario para pubicación masiva', 'S','A' from bodba.bo_comandos;
insert into bodba.BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bodba.bo_comandos where  nombre_clase = 'ViewPubMasivaForm'), 65);
insert into bodba.BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bodba.bo_comandos where  nombre_clase = 'ViewPubMasivaForm'), 1);



insert into bodba.BO_COMANDOS (ID_COMANDO, NOMBRE_CLASE, DESCRIPCION, SEGURIDAD, ACTIVO)
select max(id_comando)+1, 'AddPubMasiva','Pubicación o Despublicación masiva', 'S','A' from bodba.bo_comandos;
insert into bodba.BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bodba.bo_comandos where  nombre_clase = 'AddPubMasiva'), 65);
insert into bodba.BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bodba.bo_comandos where  nombre_clase = 'AddPubMasiva'), 1);



insert into bodba.BO_COMANDOS (ID_COMANDO, NOMBRE_CLASE, DESCRIPCION, SEGURIDAD, ACTIVO)
select max(id_comando)+1, 'ViewArbolCategorias','Vista de categorias como árbol', 'S','A' from bodba.bo_comandos;
insert into bodba.BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bodba.bo_comandos where  nombre_clase = 'ViewArbolCategorias'), 65);
insert into bodba.BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bodba.bo_comandos where  nombre_clase = 'ViewArbolCategorias'), 1);

