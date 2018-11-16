@echo off
set a=%~dp0res
set b=%~dp0res
java -jar EmailCollection.jar "%a%\urlList.txt" "%a%\email.txt"
pause