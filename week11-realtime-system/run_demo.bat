@echo off
echo ============================================
echo   Week 11 - Realtime System Demo
echo ============================================
echo.

:menu
echo [1] PriorityDemo
echo [2] DeadlineDemo
echo [3] SchedulingDemo
echo [4] RealTimeScheduler
echo [5] DeadlineManager
echo [6] Run all
echo [0] Exit
echo.

set /p choice="Enter your choice: "

if "%choice%"=="1" mvn compile exec:java -Dexec.mainClass="exercises.PriorityDemo" && goto menu
if "%choice%"=="2" mvn compile exec:java -Dexec.mainClass="exercises.DeadlineDemo" && goto menu
if "%choice%"=="3" mvn compile exec:java -Dexec.mainClass="exercises.SchedulingDemo" && goto menu
if "%choice%"=="4" mvn compile exec:java -Dexec.mainClass="projects.RealTimeScheduler" && goto menu
if "%choice%"=="5" mvn compile exec:java -Dexec.mainClass="projects.DeadlineManager" && goto menu
if "%choice%"=="6" goto all
if "%choice%"=="0" goto end

echo Invalid choice!
goto menu

:all
mvn compile exec:java -Dexec.mainClass="exercises.PriorityDemo"
mvn compile exec:java -Dexec.mainClass="exercises.DeadlineDemo"
mvn compile exec:java -Dexec.mainClass="exercises.SchedulingDemo"
mvn compile exec:java -Dexec.mainClass="projects.RealTimeScheduler"
mvn compile exec:java -Dexec.mainClass="projects.DeadlineManager"
goto menu

:end
pause

