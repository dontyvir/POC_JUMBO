PROCESS_HOME=/u02/WebSphere/AppServer/profiles/default/procesos/chaordic
cd ${PROCESS_HOME}/bin

java -Xmx512m -Djava.ext.dirs=${PROCESS_HOME}/lib:${PROCESS_HOME}/bin cl.cencosud.procesos.chaordic.main.GenerarXMLChaordic

gzip -c /u02/WebSphere/AppServer/profiles/default/procesos/chaordic/archivos/file.xml > /u02/WebSphere/AppServer/profiles/default/procesos/chaordic/archivos/file.xml.gz

#scp /u02/WebSphere/AppServer/profiles/default/procesos/chaordic/archivos/file.xml.gz wasuser@172.18.145.33:/u02/IHS/htdocs/en_US/FO/chaordic/

echo "inician los comandos remotos."
#ssh wasuser@172.18.145.33 "uname -a; gunzip /u02/IHS/htdocs/en_US/FO/chaordic/file.xml.gz;"
echo "Terminado"
