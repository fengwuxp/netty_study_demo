@echo off

::协议文件路径, 最后不要跟“\”符号
set SOURCE_FOLDER=.

::Java编译器路径
set JAVA_COMPILER_PATH=.\protoc.exe
::Java文件生成路径, 最后不要跟“\”符号
set JAVA_TARGET_PATH=.\java


::遍历所有文件
for /f "delims=" %%i in ('dir /b "%SOURCE_FOLDER%\*.proto"') do (


    ::生成 Java 代码
    echo %JAVA_COMPILER_PATH% --java_out=%JAVA_TARGET_PATH% %%i
    %JAVA_COMPILER_PATH% --java_out=%JAVA_TARGET_PATH% %%i
    
)

echo 协议生成完毕。

pause