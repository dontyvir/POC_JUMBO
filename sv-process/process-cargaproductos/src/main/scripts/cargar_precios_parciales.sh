cd /home/was/archivos
##rm JCPJ???* 2>> /u02/WebSphere/AppServer/profiles/default/procesos/carga_Productos/logs/logprecios.txt

cd /u02/cargaftp

cp JCPJ???* /home/was/backup/. 2>> /u02/WebSphere/AppServer/profiles/default/procesos/carga_Productos/logs/logprecios.txt

mv JCPJ???* /home/was/archivos/. 2>> /u02/WebSphere/AppServer/profiles/default/procesos/carga_Productos/logs/logprecios.txt

cd /home/was/archivos

rm JCPJ???*.trg 2>> /u02/WebSphere/AppServer/profiles/default/procesos/carga_Productos/logs/logprecios.txt
gzip -d JCPJ???*.gz 2>> /u02/WebSphere/AppServer/profiles/default/procesos/carga_Productos/logs/logprecios.txt

PROCESS_HOME=/u02/WebSphere/AppServer/profiles/default/procesos/carga_Productos
cd ${PROCESS_HOME}/bin

##java -Xmx512m -Djava.ext.dirs=${PROCESS_HOME}/lib:${PROCESS_HOME}/bin cl.cencosud.procesos.CargarPrecios >> /u02/WebSphere/AppServer/profiles/default/procesos/carga_Productos/logs/precios_parciales.log
java -Xmx512m -Djava.ext.dirs=${PROCESS_HOME}/lib:${PROCESS_HOME}/bin cl.cencosud.procesos.CargarPrecios

##cd /u02/WebSphere/AppServer/profiles/default/procesos/carga_Productos/bin
##java -Xmx512m -classpath %CLASSPATH%:../lib/activation-1.1.jar:../lib/commons-beanutils-1.6.1.jar:../lib/commons-collections-3.2.jar:../lib/commons-dbcp-1.2.1.jar:../lib/commons-fileupload-1.2.jar:../lib/commons-io-1.4.jar:../lib/commons-lang-2.1.jar:../lib/commons-logging-1.1.1.jar:../lib/commons-pool-1.3.jar:../lib/db2jcc-8.1.jar:../lib/db2jcc_license_cisuz-8.1.jar:../lib/db2jcc_license_cu-8.1.jar:../lib/db2jcc_license_cu-8.1.jar:../lib/ezmorph-1.0.6.jar:../lib/fastm-1.0.jar:../lib/javax.mail-1.4.7.jar:../lib/json-lib-2.2.3-jdk13.jar:../lib/log4j-1.2.13.jar:../lib/lucene-core-2.4.0.jar:../lib/lucene-snowball-2.4.0.jar:../lib/lucene-spellchecker-2.4.0.jar:../lib/poi-3.2-FINAL.jar:../lib/poi-contrib-3.2-FINAL.jar:../lib/poi-scratchpad-3.2-FINAL.jar:../lib/xercesImpl-2.0.2.jar:../lib/xml-apis-1.0.b2.jar:./ cl.cencosud.procesos.CargarPrecios >> /u02/WebSphere/AppServer/profiles/default/procesos/carga_Productos/logs/precios_parciales.log
