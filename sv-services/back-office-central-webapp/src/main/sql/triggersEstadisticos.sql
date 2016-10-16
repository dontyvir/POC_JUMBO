CREATE TRIGGER BODBA.U_BO_
UPDATE ON BODBA.BO_PEDIDOS 
REFERENCING NEW AS N 
FOR EACH ROW MODE DB2SQL 
BEGIN ATOMIC
        if N.id_estado = 10 then
            update fodba.fo_direcciones set DIR_FNUEVA = null where dir_id = N.dir_id;
        end if;
END#
