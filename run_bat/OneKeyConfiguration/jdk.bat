@echo off


%1 mshta vbscript:CreateObject("Shell.Application").ShellExecute("cmd.exe","/c %~s0 ::","","runas",1)(window.close)&&exit

set myjdkpath=C:\Java\jdk1.8

echo **********************************************
echo.
echo                   将要安装jdk
echo.
echo       安装请按任意键，退出直接关闭窗口
echo.
echo **********************************************

pause

echo.
echo 正在安装jdk，请不要执行其他操作
echo.
echo 请稍等，这个时间大约需要三、四分钟
echo.
start /WAIT jdk-8u141-windows-i586.exe /qn INSTALLDIR=C:\Java\jdk1.8
echo jdk安装完毕

set JAVA_HOME=C:\Java\jdk1.8
set PATH=%PATH%;%%JAVA_HOME%%\bin;%%JAVA_HOME%%\jre\bin
set CLASSPATH=.;%%JAVA_HOME%%\lib\dt.jar;%%JAVA_HOME%%\lib\tools.jar

set RegV=HKLM\SYSTEM\CurrentControlSet\Control\Session Manager\Environment

reg add "%RegV%" /v "JAVA_HOME" /d "%JAVA_HOME%" /f
reg add "%RegV%" /v "Path" /t REG_EXPAND_SZ /d "%PATH%" /f
reg add "%RegV%" /v "CLASSPATH" /d "%CLASSPATH%" /f
mshta vbscript:msgbox("Java环境已成功注册！",64,"成功")(window.close)

#-Dfile.encoding=utf-8

exit
