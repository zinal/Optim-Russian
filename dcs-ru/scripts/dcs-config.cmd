rem https://www.ibm.com/docs/en/iis/11.7?topic=cdc-creating-custom-data-class-that-contains-java-classifier

set IAUSER=isadmin
set IAPASS="P@ssw0rd"
set IISURL="https://localhost:9443"
set IAADMIN=ASBNode\bin\IAAdmin.bat
set IISROOT=C:\IBM\InformationServer
set IISJAR1=ASBNode\lib\java
set IISJAR2=wlp\usr\servers\iis\lib\iis\ia
set JARDST=ia-dcs-ru.jar
set JARSRC=dcs-ru-1.0-SNAPSHOT-bundle.jar

set JARNAME=%IISROOT%\%IISJAR1%\%JARDST%
