@ECHO OFF
CLS

:: Set variables
CALL config.bat

:: Make sure database is run from here
CD ..\database

ECHO Run Derby Server Script:
ECHO ------------------------

:: Apply SQL commands
java -jar "%JAVA_LIB_DIR%\derbyrun.jar" ij < ../database/schema-clear.sql

PAUSE