@echo off
echo ============================================
echo   Week 13 - ExecutorService + CompletableFuture
echo ============================================
echo.

:menu
echo Select demo to run:
echo.
echo [1] ExecutorCompletableFutureDemo - Basic combination
echo [2] PipelineDemo - Pipeline processing
echo [3] AsyncServiceOrchestrator - Advanced service orchestration
echo [4] DataPipelineProcessor - Multi-stage data pipeline
echo [5] Run all
echo [0] Exit
echo.

set /p choice="Enter your choice: "

if "%choice%"=="1" goto basic
if "%choice%"=="2" goto pipeline
if "%choice%"=="3" goto orchestrator
if "%choice%"=="4" goto datapipeline
if "%choice%"=="5" goto all
if "%choice%"=="0" goto end

echo Invalid choice!
goto menu

:basic
echo.
echo === Running ExecutorCompletableFutureDemo ===
mvn compile exec:java -Dexec.mainClass="exercises.ExecutorCompletableFutureDemo"
goto menu

:pipeline
echo.
echo === Running PipelineDemo ===
mvn compile exec:java -Dexec.mainClass="exercises.PipelineDemo"
goto menu

:orchestrator
echo.
echo === Running AsyncServiceOrchestrator ===
mvn compile exec:java -Dexec.mainClass="projects.AsyncServiceOrchestrator"
goto menu

:datapipeline
echo.
echo === Running DataPipelineProcessor ===
mvn compile exec:java -Dexec.mainClass="projects.DataPipelineProcessor"
goto menu

:all
echo.
echo === Running all demos ===
echo.
echo --- ExecutorCompletableFutureDemo ---
mvn compile exec:java -Dexec.mainClass="exercises.ExecutorCompletableFutureDemo"
echo.
echo --- PipelineDemo ---
mvn compile exec:java -Dexec.mainClass="exercises.PipelineDemo"
echo.
echo --- AsyncServiceOrchestrator ---
mvn compile exec:java -Dexec.mainClass="projects.AsyncServiceOrchestrator"
echo.
echo --- DataPipelineProcessor ---
mvn compile exec:java -Dexec.mainClass="projects.DataPipelineProcessor"
goto menu

:end
echo.
echo Goodbye!
pause

