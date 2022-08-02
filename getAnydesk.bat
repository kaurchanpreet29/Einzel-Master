@echo off
for /f "delims=" %%i in ('"C:\Program Files (x86)\AnyDesk\AnyDesk.exe" --get-id') do set CID=%%i 
echo AnyDesk ID is: %CID%
pause