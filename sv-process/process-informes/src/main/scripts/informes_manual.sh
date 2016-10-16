cd /home/was/programas_consolas/informes/bin
echo "********** echo ************** INICIANDO InformesManualBOC ---> "

DIA_INI=$1
MES_INI=$2
DIA_FIN=$3
MES_FIN=$4
AGNO_INI=$5
AGNO_FIN=$6

nohup java -Xmx128m -classpath ../lib/log4j-1.2.15.jar:../lib/commons-lang-2.1.jar:../lib/db2jcc.jar:../lib/db2jcc_license_cu.jar:../lib/db2jcc_license_cisuz.jar:../lib/mail.jar:../lib/activation.jar:../lib/poi-3.2-FINAL-20081019.jar:../lib/poi-contrib-3.2-FINAL-20081019.jar:../lib/poi-scratchpad-3.2-FINAL-20081019.jar:. cl.cencosud.informes.manual.InformesManualBOC $DIA_INI $MES_INI $DIA_FIN $MES_FIN $AGNO_INI $AGNO_FIN & >> logs.txt

