cd /u02/cargaftp
cp CVD* /home/was/backup/.
mv CVD* /home/was/archivos/.
cd /home/was/archivos
rm CVD*.trg
gzip -d CVD*.gz

PROCESS_HOME=/u02/WebSphere/AppServer/profiles/default/procesos/carga_Productos
cd ${PROCESS_HOME}/bin

java -Xmx512m -Djava.ext.dir=${PROCESS_HOME}/lib:${PROCESS_HOME}/bin cl.cencosud.procesos.CargarSaldoEmpresa >> cargar_saldo_empresa.log

cd /home/was/archivos
rm CVD*
