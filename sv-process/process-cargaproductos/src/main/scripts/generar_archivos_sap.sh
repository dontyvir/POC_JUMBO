java -classpath %CLASSPATH%:../lib/db2jcc.jar:../lib/db2jcc_license_cu.jar:../lib/db2jcc_license_cisuz.jar:../lib/commons-lang-2.1.jar:./ cl.cencosud.archivossap.GenerarArchivoProductos >> generar_archivos_productos.log
cd /u02/cargaftp/archivos_para_sap
gzip *.txt
echo "copia archivos a almendro por ftp"
cd /u02/WebSphere/AppServer/profiles/default/procesos/carga_Productos/bin/script   
./ejecutar.sh >> /u02/WebSphere/AppServer/profiles/default/procesos/carga_Productos/logs/archivos_sap.log
