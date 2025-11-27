@echo off
echo ============================================
echo   Week 4 - Executor và ThreadPool Demo
echo ============================================
echo.

:menu
echo Select demo to run:
echo.
echo [1] ExecutorDemo - Executor Framework basics
echo [2] ThreadPoolDemo - Thread pool types
echo [3] ScheduledExecutorDemo - Scheduled tasks
echo [4] WebServer - Web server simulator
echo [5] TaskProcessor - Batch task processing
echo [6] Run all
echo [0] Exit
echo.

set /p choice="Enter your choice: "

if "%choice%"=="1" goto executor
if "%choice%"=="2" goto threadpool
if "%choice%"=="3" goto scheduled
if "%choice%"=="4" goto webserver
if "%choice%"=="5" goto taskprocessor
if "%choice%"=="6" goto all
if "%choice%"=="0" goto end

echo Invalid choice!
goto menu

:executor
echo.
echo === Running ExecutorDemo ===
mvn compile exec:java -Dexec.mainClass="exercises.ExecutorDemo"
goto menu

:threadpool
echo.
echo === Running ThreadPoolDemo ===
mvn compile exec:java -Dexec.mainClass="exercises.ThreadPoolDemo"
goto menu

:scheduled
echo.
echo === Running ScheduledExecutorDemo ===
mvn compile exec:java -Dexec.mainClass="exercises.ScheduledExecutorDemo"
goto menu

:webserver
echo.
echo === Running WebServer ===
mvn compile exec:java -Dexec.mainClass="projects.WebServer"
goto menu

:taskprocessor
echo.
echo === Running TaskProcessor ===
mvn compile exec:java -Dexec.mainClass="projects.TaskProcessor"
goto menu

:all
echo.
echo === Running all demos ===
echo.
echo --- ExecutorDemo ---
mvn compile exec:java -Dexec.mainClass="exercises.ExecutorDemo"
echo.
echo --- ThreadPoolDemo ---
mvn compile exec:java -Dexec.mainClass="exercises.ThreadPoolDemo"
echo.
echo --- ScheduledExecutorDemo ---
mvn compile exec:java -Dexec.mainClass="exercises.ScheduledExecutorDemo"
echo.
echo --- WebServer ---
mvn compile exec:java -Dexec.mainClass="projects.WebServer"
echo.
echo --- TaskProcessor ---
mvn compile exec:java -Dexec.mainClass="projects.TaskProcessor"
goto menu

:end
echo.
echo Goodbye!
pause

