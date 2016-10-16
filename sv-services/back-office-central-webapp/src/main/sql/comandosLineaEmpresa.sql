
--************************ COMANDOS PARA BOC ***********************************
insert into bodba.bo_comandos 
(id_comando,nombre_clase, descripcion, seguridad, activo)
values
(563,'ViewEmpresaLineaForm','Acceso mod Linea Credito [BOC]','S','A');

insert into bodba.bo_comandos 
(id_comando,nombre_clase, descripcion, seguridad, activo)
values
(564,'ModEmpresaLinea','Modificar Linea Credito [BOC]','S','A');


--************************* PERMISOS PARA ADMINISTRADOR **********************************
INSERT INTO BODBA.BO_COMXPERF
(ID_COMANDO,ID_PERFIL) VALUES (563,1);

INSERT INTO BODBA.BO_COMXPERF
(ID_COMANDO,ID_PERFIL) VALUES (564,1);


--************************* TABLAS **********************************
CREATE TABLE FODBA.VE_EMPRESA_LINEA_LOG (
    ID INTEGER	NOT NULL GENERATED ALWAYS AS IDENTITY ( START WITH 1, INCREMENT BY 1, NO CYCLE, MINVALUE 1, MAXVALUE 2147483647, NO CACHE ),
    ID_EMPRESA	INTEGER,
    ID_USUARIO	INTEGER,
    EMP_OLD_SALDO	DECIMAL(12,2),
    EMP_NEW_SALDO	DECIMAL(12,2),
    FECHA		TIMESTAMP DEFAULT CURRENT TIMESTAMP,
    CONSTRAINT PK_EMPLIN
    PRIMARY KEY (ID)
) IN FO_TABLESPACE
;
ALTER TABLE FODBA.VE_EMPRESA_LINEA_LOG ADD CONSTRAINT FK_EMPLIN_EMP 
    FOREIGN KEY (ID_EMPRESA)
    REFERENCES FODBA.VE_EMPRESA(EMP_ID)
;
ALTER TABLE FODBA.VE_EMPRESA_LINEA_LOG ADD CONSTRAINT FK_EMPLIN_USU 
    FOREIGN KEY (ID_USUARIO)
    REFERENCES BODBA.BO_USUARIOS(ID_USUARIO)
;