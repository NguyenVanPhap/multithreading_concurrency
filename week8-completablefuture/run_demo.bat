@echo off
echo ============================================
echo   Week 8 - CompletableFuture Demo
echo ============================================
echo.

:menu
echo [1] CompletableFutureDemo
echo [2] ChainingDemo
echo [3] CombiningDemo
echo [4] AsyncAPIClient
echo [5] PipelineProcessor
echo [6] Run all
echo [0] Exit
echo.

set /p choice="Enter your choice: "

if "%choice%"=="1" mvn compile exec:java -Dexec.mainClass="exercises.CompletableFutureDemo" && goto menu
if "%choice%"=="2" mvn compile exec:java -Dexec.mainClass="exercises.ChainingDemo" && goto menu
if "%choice%"=="3" mvn compile exec:java -Dexec.mainClass="exercises.CombiningDemo" && goto menu
if "%choice%"=="4" mvn compile exec:java -Dexec.mainClass="projects.AsyncAPIClient" && goto menu
if "%choice%"=="5" mvn compile exec:java -Dexec.mainClass="projects.PipelineProcessor" && goto menu
if "%choice%"=="6" goto all
if "%choice%"=="0" goto end

echo Invalid choice!
goto menu

:all
mvn compile exec:java -Dexec.mainClass="exercises.CompletableFutureDemo"
mvn compile exec:java -Dexec.mainClass="exercises.ChainingDemo"
mvn compile exec:java -Dexec.mainClass="exercises.CombiningDemo"
mvn compile exec:java -Dexec.mainClass="projects.AsyncAPIClient"
mvn compile exec:java -Dexec.mainClass="projects.PipelineProcessor"
goto menu

:end
pause

