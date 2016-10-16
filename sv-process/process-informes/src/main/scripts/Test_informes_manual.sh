cd /home/was/programas_consolas/informes/bin
echo "echo Llamando TestInformesManualBOC ---> "

nohup java -Xmx128m -classpath ../lib/log4j-1.2.15.jar:../lib/commons-lang-2.1.jar:../lib/db2jcc.jar:../lib/db2jcc_license_cu.jar:../lib/db2jcc_license_cisuz.jar:../lib/mail.jar:../lib/activation.jar:../lib/poi-3.2-FINAL-20081019.jar:../lib/poi-contrib-3.2-FINAL-20081019.jar:../lib/poi-scratchpad-3.2-FINAL-20081019.jar:. cl.cencosud.informes.manual.TestInformesManualBOC & >> logs.txt

