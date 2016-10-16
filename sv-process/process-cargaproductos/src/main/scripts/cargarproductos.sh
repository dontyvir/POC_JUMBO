cd /home/was/archivos
rm *

cd /u02/cargaftp

if [ $1 ]
then
   fecha=$1
else

   dia=`date +"%d"`

   dia=$(($dia-1))

   if [ $dia = 0 ]
   then
      dia=1
   fi

   if [ $dia -lt 10 ]
   then
      dia=0$dia
   fi

   mes=`date +"%m"`
   fecha=$mes$dia
fi

echo "copiar archivos con fecha "$fecha

cp ???????$fecha* /home/was/archivos/.

cd /home/was/archivos
rm *.trg
gzip -d *.gz

cd /u02/WebSphere/AppServer/profiles/default/procesos/carga_Productos/bin

java -Xmx1024m -classpath %CLASSPATH%:../lib/db2jcc.jar:../lib/db2jcc_license_cu.jar:../lib/db2jcc_license_cisuz.jar:../lib/commons-lang-2.1.jar:./ cl.cencosud.procesos.Cargar >> cargarProductos.log
