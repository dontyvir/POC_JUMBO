##ruta de ejecución del programa
cd /home/was/kettle_sh/programas_consola/ranking/bin
java -Xmx512m -classpath ../lib/db2jcc.jar:../lib/db2jcc_license_cu.jar:../lib/db2jcc_license_cisuz.jar:. cl.cencosud.ultimascompras.CargarUltimasCompras -1 >> mover_ultimas_compras.log
