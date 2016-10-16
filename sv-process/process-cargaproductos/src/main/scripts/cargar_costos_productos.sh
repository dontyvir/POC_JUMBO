cd /u02/cargaftp
cp CVC* /home/was/backup/.
mv CVC* /home/was/archivos/.
cd /home/was/archivos
rm CVC*.trg
gzip -d CVC*.gz

PROCESS_HOME=/u02/WebSphere/AppServer/profiles/default/procesos/carga_Productos
cd ${PROCESS_HOME}/bin

java -Xmx512m -Djava.ext.dir=${PROCESS_HOME}/lib:${PROCESS_HOME}/bin cl.cencosud.procesos.CargarCostos

cd /home/was/archivos
rm CVC*