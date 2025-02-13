@ECHO OFF
CLS

:: Set variables
CALL config.bat

:: Make sure database is run from here
CD ..\database

ECHO Run Derby Database Server:
ECHO --------------------------

:: Run derby database server
java -jar "%JAVA_LIB_DIR%\derbyrun.jar" server start

PAUSE