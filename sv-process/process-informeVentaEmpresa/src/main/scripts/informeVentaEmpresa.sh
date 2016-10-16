##!/bin/bash
#Configurar segun ambiente.
#environment="PRODUCCION"
environment="test"

MYPATH=""

##PATH TEST 
PATH_TEST=/u02/WebSphere/AppServer/profiles/default/informes/informeVentaempresa/
##PATH PRODUCCION
PATH_PRODUCCION=/u02/WebSphere/AppServer/profiles/default/informes/informeVentaempresa/

if [ ${environment} = 'PRODUCCION' ];
then
MYPATH=${PATH_PRODUCCION}
else
MYPATH=${PATH_TEST}
fi

cd ${MYPATH}bin/
java -Xmx128m -Djava.ext.dirs=${MYPATH}lib:./ cl.cencosud.jumbo.main.InformeVentaEmpresa $1 $2

