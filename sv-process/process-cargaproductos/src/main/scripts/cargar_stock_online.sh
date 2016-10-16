PROCESS_HOME=/u02/WebSphere/AppServer/profiles/default/procesos/carga_Productos
cd ${PROCESS_HOME}/bin

java -Xmx512m -Djava.ext.dir=${PROCESS_HOME}/lib:${PROCESS_HOME}/bin cl.cencosud.procesos.CargarStockOnline fin