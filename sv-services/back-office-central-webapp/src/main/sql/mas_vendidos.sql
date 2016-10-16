insert into bodba.BO_COMANDOS (ID_COMANDO, NOMBRE_CLASE, DESCRIPCION, SEGURIDAD, ACTIVO)
select max(id_comando)+1, 'ViewMasvCategorias','ViewMasvCategorias', 'S','A'  from bodba.bo_comandos;
insert into bodba.BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bodba.bo_comandos where  nombre_clase = 'ViewMasvCategorias'), 1);

insert into bodba.BO_COMANDOS (ID_COMANDO, NOMBRE_CLASE, DESCRIPCION, SEGURIDAD, ACTIVO)
select max(id_comando)+1, 'MasvCategoriasEdit','MasvCategoriasEdit', 'S','A'  from bodba.bo_comandos;
insert into bodba.BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bodba.bo_comandos where  nombre_clase = 'MasvCategoriasEdit'), 1);

insert into bodba.BO_COMANDOS (ID_COMANDO, NOMBRE_CLASE, DESCRIPCION, SEGURIDAD, ACTIVO)
select max(id_comando)+1, 'MasvCategoriasUpd','MasvCategoriasUpd', 'S','A'  from bodba.bo_comandos;
insert into bodba.BO_COMXPERF(ID_COMANDO, ID_PERFIL) 
values ( (select id_comando from bodba.bo_comandos where  nombre_clase = 'MasvCategoriasUpd'), 1);



drop table fodba.masv_categorias;
create table fodba.masv_categorias(
	categoria_id integer not null,
	activo_masv smallint not null default 1,
	activo_banner smallint not null default 0,
	fecha_inicio date,
	fecha_termino date,
	banner_principal varchar(100),
	banner_secundario1 varchar(100),
	banner_secundario2 varchar(100),
	semana integer default 0,
	primary key(categoria_id),
	CONSTRAINT fk_masv_cat_id FOREIGN KEY(categoria_id) REFERENCES bodba.fo_categorias(cat_id) ON DELETE CASCADE
);

drop table fodba.masv_productos;
create table fodba.masv_productos(
	masv_categoria_id integer not null,
    local_id integer not null,
    producto_id integer not null,
    cantidad integer not null,
    CONSTRAINT fk_masv_pro_cat_id FOREIGN KEY(masv_categoria_id) REFERENCES fodba.masv_categorias(categoria_id) ON DELETE CASCADE,
    CONSTRAINT fk_masv_pro_loc_id FOREIGN KEY(local_id) REFERENCES bodba.bo_locales(id_local) ON DELETE CASCADE,
    CONSTRAINT fk_masv_pro_pro_id FOREIGN KEY(producto_id) REFERENCES fodba.fo_productos(pro_id) ON DELETE CASCADE
);

insert into fodba.fo_productos_masven (local_id, pro_id, cantidad)
select pe.id_local, fp.pro_id, count(distinct pe.id_pedido) as cantidad
from bodba.bo_pedidos pe inner join bodba.bo_detalle_pedido dp on pe.id_pedido = dp.id_pedido
inner join fodba.fo_productos fp on fp.pro_id_bo = dp.id_producto
where pe.created_at >= '2010-04-05' and pe.created_at <= '2010-04-12'
group by pe.id_local, fp.pro_id




select distinct ROW_NUMBER() OVER (order by mas.cantidad desc) AS row_number,
pe.id_local, fp.pro_id, count(distinct pe.id_pedido) as cantidad
from bodba.bo_pedidos pe inner join bodba.bo_detalle_pedido dp on pe.id_pedido = dp.id_pedido
inner join fodba.fo_productos fp on fp.pro_id_bo = dp.id_producto
where pe.created_at >= '2010-04-05' and pe.created_at <= '2010-04-12'
group by pe.id_local, fp.pro_id







select pro.pro_id, pro.pro_tipo_producto, pro.pro_des_corta, pro_tipre,                
pro.pro_imagen_minificha, mar.mar_nombre, mar.mar_id, PRO_INTER_VALOR, PRO_INTER_MAX,     
uni.uni_desc, pro_nota, car_nota, pro_id_bo, pro_particionable, pro_particion, pre_valor
from fodba.fo_productos_masven mas
inner join fodba.fo_productos pro on mas.pro_id = pro.pro_id
inner join fodba.fo_precios_locales pre on pro.pro_id = pre.pre_pro_id   and mas.local_id = pre.pre_loc_id 
inner join fodba.fo_marcas mar on mar.mar_id = pro.pro_mar_id   
inner join fodba.fo_unidades_medida uni on uni.uni_id = pro.pro_uni_id   
inner join fodba.fo_productos_categorias subcat on subcat.prca_pro_id = pro.pro_id
inner join fodba.fo_catsubcat cat on cat.subcat_id = subcat.prca_cat_id
left outer join fodba.fo_carro_compras car on car.car_pro_id = pro.pro_id and car_cli_id = 1
where pre.pre_estado = 'A' and pro.pro_estado = 'A' and pro_inter_valor > 0            
and pre_loc_id = 1 and cat.cat_id = 2
order by mas.cantidad desc