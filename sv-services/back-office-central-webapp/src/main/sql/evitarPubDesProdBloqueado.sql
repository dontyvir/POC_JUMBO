--Marca para evitar que un productos de publique o despublique cuando esta bloqueado.

alter table fodba.fo_productos add column evitar_pub_des char(1) not null default 'N'