@echo off
set /p myip=<ip.txt
for /f "tokens=1-2 delims=:" %%a in ('ipconfig^|find "IPv4"') do set ip=%%b
set ip=%ip:~1%
@echo %ip% > ip.txt
server --cl --dir "%CD%" --fileMask "*.properties" --excludeFileMask "*.zip, *.rar, *.7z, *.jar, *.dll, *.exe" --includeSubDirectories --useRegEx --find "\b\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}\b" --replace %ip%
pause