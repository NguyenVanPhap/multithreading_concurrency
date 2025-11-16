@echo off
echo ============================================
echo   Week 7 - Atomic và Lock-free Demo
echo ============================================
echo.

:menu
echo [1] AtomicDemo
echo [2] AtomicReferenceDemo
echo [3] LockFreeDemo
echo [4] LockFreeStack
echo [5] LockFreeCounter
echo [6] Run all
echo [0] Exit
echo.

set /p choice="Enter your choice: "

if "%choice%"=="1" mvn compile exec:java -Dexec.mainClass="exercises.AtomicDemo" && goto menu
if "%choice%"=="2" mvn compile exec:java -Dexec.mainClass="exercises.AtomicReferenceDemo" && goto menu
if "%choice%"=="3" mvn compile exec:java -Dexec.mainClass="exercises.LockFreeDemo" && goto menu
if "%choice%"=="4" mvn compile exec:java -Dexec.mainClass="projects.LockFreeStack" && goto menu
if "%choice%"=="5" mvn compile exec:java -Dexec.mainClass="projects.LockFreeCounter" && goto menu
if "%choice%"=="6" goto all
if "%choice%"=="0" goto end

echo Invalid choice!
goto menu

:all
mvn compile exec:java -Dexec.mainClass="exercises.AtomicDemo"
mvn compile exec:java -Dexec.mainClass="exercises.AtomicReferenceDemo"
mvn compile exec:java -Dexec.mainClass="exercises.LockFreeDemo"
mvn compile exec:java -Dexec.mainClass="projects.LockFreeStack"
mvn compile exec:java -Dexec.mainClass="projects.LockFreeCounter"
goto menu

:end
pause

