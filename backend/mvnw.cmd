@echo off
setlocal
set BASE_DIR=%~dp0
set MAVEN_VERSION=3.9.9
set MAVEN_DIR=%BASE_DIR%.mvn\apache-maven-%MAVEN_VERSION%
set MAVEN_ZIP=%BASE_DIR%.mvn\apache-maven-%MAVEN_VERSION%-bin.zip
if not exist "%MAVEN_DIR%\bin\mvn.cmd" (
  echo Downloading Apache Maven %MAVEN_VERSION%...
  powershell -NoProfile -ExecutionPolicy Bypass -Command "Invoke-WebRequest -Uri 'https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/%MAVEN_VERSION%/apache-maven-%MAVEN_VERSION%-bin.zip' -OutFile '%MAVEN_ZIP%'; Expand-Archive -LiteralPath '%MAVEN_ZIP%' -DestinationPath '%BASE_DIR%.mvn' -Force"
)
call "%MAVEN_DIR%\bin\mvn.cmd" %*
