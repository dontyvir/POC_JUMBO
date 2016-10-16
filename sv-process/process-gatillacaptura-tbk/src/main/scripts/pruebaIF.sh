# Se busca proceso en ejecución, se debe agregar identificado único del proceso en el primer grep
##resultado='ps -fea | grep irsmonitor | grep -v grep'
resultado=`ps -fea | grep url.Url_TBK | grep -v grep`  

#echo $resultado

#Compara resultado anterior si esta vacía la variable 

if [ "$resultado" == "" ]
then
    echo 'voy a ejecutar el proceso' >> logs.txt
    cd /u02/WebSphere/AppServer/profiles/default/deamons/GatillaCapturaTBK
    #java -Djava.awt.headless=true -cp  .:./lib/db2jcc.jar:./lib/db2jcc_license_cu.jar:./lib/db2jcc_license_cisuz.jar:./lib/GatillaCapturaTBK.jar cl.jumbo.url.Url_TBK >> logs.txt
else
    echo 'El proceso esta en ejecucion, NO se ejecuta nuevamente hasta terminar el actual' >> logs.txt
fi
