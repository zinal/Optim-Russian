# https://www.ibm.com/docs/en/iis/11.7?topic=cdc-creating-custom-data-class-that-contains-java-classifier
# into dsenv: OPTIM_DCS_DICT=/path/to/dicts/dir

IAUSER=isadmin
IAPASS="P@ssw0rd"
IISURL="https://localhost:9443"
IAADMIN=ASBNode/bin/IAAdmin.sh
IISROOT=/datum/sw/InformationServer
IISJAR1=ASBNode/lib/java
IISJAR2=wlp/usr/servers/iis/lib/iis/ia
JARDST=iacheck-bundle.jar
JARSRC=iacheck-1.0-SNAPSHOT-bundle.jar

JARNAME="$IISROOT"/"$IISJAR1"/"$JARDST"
