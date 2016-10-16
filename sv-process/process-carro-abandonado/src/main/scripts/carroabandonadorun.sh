environment=PRODUCCION
#environment=test
MYPATH=''

## ruta template mail : /u02/WebSphere/AppServer/profiles/default/procesos/carroAbandonado/tmp/carro_abandonado/
#html.cupon.directorio=  /u02/WebSphere/AppServer/profiles/default/procesos/carroAbandonado/tmp/carro_abandonado/
#html.cupon.descuento=carro_dcto.html
#html.cupon.despacho=carro_despacho.html
#html.cupon.recuerda=carro_recuerda.html

##MYPATH PRODUCCION
MYPATH_PRODUCCION=/u02/WebSphere/AppServer/profiles/default/procesos/carroAbandonado/
IP_CEIBO=172.18.145.33
USER_CEIBO=wasuser
DIR_TEMPLATE_CEIBO=/u02/IHS/htdocs/en_US/supermercado/carro_abandonado/*
DIR_TEMPLATE_COPIHUE=/u02/WebSphere/AppServer/profiles/default/procesos/carroAbandonado/tmp/carro_abandonado/

if [ ${environment} = 'PRODUCCION' ];
then
MYPATH=${MYPATH_PRODUCCION}
#scp -BC ${USER_CEIBO}@${IP_CEIBO}:${DIR_TEMPLATE_CEIBO} ${DIR_TEMPLATE_COPIHUE}
scp ${USER_CEIBO}@${IP_CEIBO}:${DIR_TEMPLATE_CEIBO} ${DIR_TEMPLATE_COPIHUE}
else
MYPATH=${MYPATH_TEST}
fi

#echo ${MYPATH}
cd ${MYPATH}bin
java -Xmx128m -Djava.ext.dirs=${MYPATH}lib:./ cl.cencosud.procesos.carroabandonado.main.CarroAbandonado
