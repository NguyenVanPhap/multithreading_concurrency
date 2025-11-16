@echo off
echo ============================================
echo   Week 9 - Parallelism và ForkJoin Demo
echo ============================================
echo.

:menu
echo [1] ForkJoinDemo
echo [2] RecursiveTaskDemo
echo [3] ParallelStreamDemo
echo [4] ParallelArraySum
echo [5] ParallelMergeSort
echo [6] Run all
echo [0] Exit
echo.

set /p choice="Enter your choice: "

if "%choice%"=="1" mvn compile exec:java -Dexec.mainClass="exercises.ForkJoinDemo" && goto menu
if "%choice%"=="2" mvn compile exec:java -Dexec.mainClass="exercises.RecursiveTaskDemo" && goto menu
if "%choice%"=="3" mvn compile exec:java -Dexec.mainClass="exercises.ParallelStreamDemo" && goto menu
if "%choice%"=="4" mvn compile exec:java -Dexec.mainClass="projects.ParallelArraySum" && goto menu
if "%choice%"=="5" mvn compile exec:java -Dexec.mainClass="projects.ParallelMergeSort" && goto menu
if "%choice%"=="6" goto all
if "%choice%"=="0" goto end

echo Invalid choice!
goto menu

:all
mvn compile exec:java -Dexec.mainClass="exercises.ForkJoinDemo"
mvn compile exec:java -Dexec.mainClass="exercises.RecursiveTaskDemo"
mvn compile exec:java -Dexec.mainClass="exercises.ParallelStreamDemo"
mvn compile exec:java -Dexec.mainClass="projects.ParallelArraySum"
mvn compile exec:java -Dexec.mainClass="projects.ParallelMergeSort"
goto menu

:end
pause

