#!/bin/bash
#Configurar segun ambiente.
#environment="PRODUCCION"
environment="test"


MYPATH=""
#0:fecha actual; -1:ayer
PARAM=0
##PATH TEST 
PATH_TEST=/home/was/programa_consola/informes/mail-cambioPrecio-PM/
##PATH PRODUCCION
PATH_PRODUCCION=/u02/WebSphere/AppServer/profiles/default/informes/mail-cambioPrecio-PM/

if [ ${environment} = 'PRODUCCION' ];
then
MYPATH=${PATH_PRODUCCION}
else
MYPATH=${PATH_TEST}
fi

cd ${MYPATH}bin/
java -Xmx128m -Djava.ext.dirs=${MYPATH}lib:./ cl.cencosud.jumbo.main.CambioPrecioPM ${PARAM}
