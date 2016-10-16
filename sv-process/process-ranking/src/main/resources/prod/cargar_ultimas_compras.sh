##muevo los archivos JCV a la ruta de lectura y los descomprimo
cd /u02/cargaftp
mv JCV* /home/was/backup/archivos_JCV
cd /home/was/backup/archivos_JCV
mv *.trg /home/was/backup/archivos_JCV/archivos_trg
gzip -d *.gz

##ruta de ejecucion del programa
cd /home/was/kettle_sh/programas_consola/ranking/bin
java -Xmx512m -classpath ../lib/db2jcc.jar:../lib/db2jcc_license_cu.jar:../lib/db2jcc_license_cisuz.jar:. cl.cencosud.ultimascompras.CargarUltimasCompras $1 >> cargar_ultimas_compras.log
