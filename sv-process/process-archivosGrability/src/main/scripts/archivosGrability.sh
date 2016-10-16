#!/bin/bash
#Configurar segun ambiente.
#environment="PRODUCCION"
environment="test"

MYPATH=""

##PATH TEST 
PATH_TEST=/home/was/programa_consola/interfaces_grability/process-archivosGrability/
##PATH PRODUCCION
PATH_PRODUCCION=/u02/WebSphere/AppServer/profiles/default/procesos/interfaces_grability/process-archivosGrability/

if [ ${environment} = 'PRODUCCION' ];
then
MYPATH=${PATH_PRODUCCION}
else
MYPATH=${PATH_TEST}
fi

#Opciones de ejecucion sin parametros ejecuta todas las interfaces.
#Con parametros :
#args[0].equals("C")
#ArchivoCatalogo.archivoCarga();

#args[0].equals("M")
#ArchivoComunas.archivoCarga();

#args[0].equals("S")
#ArchivoCriterios.archivoCarga();

#args[0].equals("A")
#ArchivoDisponibilidad.archivoCarga();

#args[0].equals("L")
#ArchivoLocales.archivoCarga();

#args[0].equals("P")
#ArchivoPrecios.archivoCarga();

#args[0].equals("O")
#ArchivoPromociones.archivoCarga();

#args[0].equals("R")
#ArchivoRegiones.archivoCarga();

cd ${MYPATH}bin/
java -Xmx128m -Djava.ext.dirs=${MYPATH}lib:./ cl.cencosud.procesos.GeneraInterfaces $1
