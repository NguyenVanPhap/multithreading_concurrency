@echo off
echo ============================================
echo   Week 15 - BlockingQueue + ThreadPool
echo ============================================
echo.

:menu
echo Select demo to run:
echo.
echo [1] BlockingQueueThreadPoolDemo - Basic combination
echo [2] ProducerConsumerDemo - Producer-Consumer pattern
echo [3] TaskQueueProcessor - Advanced task processing
echo [4] EventDrivenMessageBroker - Message broker system
echo [5] Run all
echo [0] Exit
echo.

set /p choice="Enter your choice: "

if "%choice%"=="1" goto basic
if "%choice%"=="2" goto producerconsumer
if "%choice%"=="3" goto taskqueue
if "%choice%"=="4" goto messagebroker
if "%choice%"=="5" goto all
if "%choice%"=="0" goto end

echo Invalid choice!
goto menu

:basic
echo.
echo === Running BlockingQueueThreadPoolDemo ===
mvn compile exec:java -Dexec.mainClass="exercises.BlockingQueueThreadPoolDemo"
goto menu

:producerconsumer
echo.
echo === Running ProducerConsumerDemo ===
mvn compile exec:java -Dexec.mainClass="exercises.ProducerConsumerDemo"
goto menu

:taskqueue
echo.
echo === Running TaskQueueProcessor ===
mvn compile exec:java -Dexec.mainClass="projects.TaskQueueProcessor"
goto menu

:messagebroker
echo.
echo === Running EventDrivenMessageBroker ===
mvn compile exec:java -Dexec.mainClass="projects.EventDrivenMessageBroker"
goto menu

:all
echo.
echo === Running all demos ===
echo.
echo --- BlockingQueueThreadPoolDemo ---
mvn compile exec:java -Dexec.mainClass="exercises.BlockingQueueThreadPoolDemo"
echo.
echo --- ProducerConsumerDemo ---
mvn compile exec:java -Dexec.mainClass="exercises.ProducerConsumerDemo"
echo.
echo --- TaskQueueProcessor ---
mvn compile exec:java -Dexec.mainClass="projects.TaskQueueProcessor"
echo.
echo --- EventDrivenMessageBroker ---
mvn compile exec:java -Dexec.mainClass="projects.EventDrivenMessageBroker"
goto menu

:end
echo.
echo Goodbye!
pause

