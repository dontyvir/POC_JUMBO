#!/bin/sh
resultado=`ps -fea |grep CargaPromoBot| grep -v grep`
#
# CargaPromoBot
#
# aplicacion que carga archivos de promociones enviados por FTP a distintos locales de jumbo.cl
# y recorre los directorios de cada local indicado en Common.jar/Constantes
# para al final reponder al IRS Promociones con un feedback de exito/fracaso de la carga de 
# la promocion via socket.
#

##PATH 
PATH_CARGABOT_PROMO=/u02/WebSphere/AppServer/profiles/default/procesos/carga_promociones/

if [ "$resultado" == "" ]
then
    cd ${PATH_CARGABOT_PROMO}
	
	nohup java -Xmx256m -Djava.ext.dirs=${PATH_CARGABOT_PROMO}lib CargaPromoBot
	
   #OPT="-Djava.awt.headless=true -Xmx256m -cp $CLASSPATH"
   #nohup java $OPT CargaPromoBot &
   
   else
      echo 'El proceso esta en ejecucion, NO se ejecuta nuevamente hasta terminar el actual' >> logs.txt
fi

