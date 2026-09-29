@echo off
setlocal
set "APP_HOME=%~dp0"
set "GRADLE_HOME=%USERPROFILE%\.gradle\aria-gradle-8.7"
if not exist "%GRADLE_HOME%\bin\gradle.bat" (
  powershell -NoProfile -Command "New-Item -ItemType Directory -Force '%USERPROFILE%\.gradle' | Out-Null; Invoke-WebRequest -Uri 'https://services.gradle.org/distributions/gradle-8.7-bin.zip' -OutFile '%USERPROFILE%\.gradle\gradle-8.7-bin.zip'; Expand-Archive -Force '%USERPROFILE%\.gradle\gradle-8.7-bin.zip' '%USERPROFILE%\.gradle\aria-gradle-temp'; Move-Item '%USERPROFILE%\.gradle\aria-gradle-temp\gradle-8.7' '%GRADLE_HOME%'"
)
call "%GRADLE_HOME%\bin\gradle.bat" -p "%APP_HOME%" %*
endlocal
