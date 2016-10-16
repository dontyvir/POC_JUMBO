PROCESS_HOME=/u02/WebSphere/AppServer/profiles/default/procesos/carga_Productos
cd ${PROCESS_HOME}/bin

java -Xmx1024m -Djava.ext.dir=${PROCESS_HOME}/lib:${PROCESS_HOME}/bin cl.cencosud.lucene.IndizadorProductos >> actualizar.log
