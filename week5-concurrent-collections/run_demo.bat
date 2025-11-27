@echo off
echo ============================================
echo   Week 5 - Concurrent Collections Demo
echo ============================================
echo.

:menu
echo [1] ConcurrentMapDemo
echo [2] BlockingQueueDemo
echo [3] CopyOnWriteDemo
echo [4] ProducerConsumer
echo [5] ConcurrentCache
echo [6] Run all
echo [0] Exit
echo.

set /p choice="Enter your choice: "

if "%choice%"=="1" mvn compile exec:java -Dexec.mainClass="exercises.ConcurrentMapDemo" && goto menu
if "%choice%"=="2" mvn compile exec:java -Dexec.mainClass="exercises.BlockingQueueDemo" && goto menu
if "%choice%"=="3" mvn compile exec:java -Dexec.mainClass="exercises.CopyOnWriteDemo" && goto menu
if "%choice%"=="4" mvn compile exec:java -Dexec.mainClass="projects.ProducerConsumer" && goto menu
if "%choice%"=="5" mvn compile exec:java -Dexec.mainClass="projects.ConcurrentCache" && goto menu
if "%choice%"=="6" goto all
if "%choice%"=="0" goto end

echo Invalid choice!
goto menu

:all
mvn compile exec:java -Dexec.mainClass="exercises.ConcurrentMapDemo"
mvn compile exec:java -Dexec.mainClass="exercises.BlockingQueueDemo"
mvn compile exec:java -Dexec.mainClass="exercises.CopyOnWriteDemo"
mvn compile exec:java -Dexec.mainClass="projects.ProducerConsumer"
mvn compile exec:java -Dexec.mainClass="projects.ConcurrentCache"
goto menu

:end
pause

