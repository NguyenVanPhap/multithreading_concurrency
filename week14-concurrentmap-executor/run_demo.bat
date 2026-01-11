@echo off
echo ============================================
echo   Week 14 - ConcurrentHashMap + ExecutorService
echo ============================================
echo.

:menu
echo Select demo to run:
echo.
echo [1] ConcurrentMapExecutorDemo - Basic combination
echo [2] MapAggregationDemo - Data aggregation
echo [3] DistributedCacheManager - Advanced cache system
echo [4] ConcurrentDataAggregator - Data aggregation system
echo [5] Run all
echo [0] Exit
echo.

set /p choice="Enter your choice: "

if "%choice%"=="1" goto basic
if "%choice%"=="2" goto aggregation
if "%choice%"=="3" goto cache
if "%choice%"=="4" goto aggregator
if "%choice%"=="5" goto all
if "%choice%"=="0" goto end

echo Invalid choice!
goto menu

:basic
echo.
echo === Running ConcurrentMapExecutorDemo ===
mvn compile exec:java -Dexec.mainClass="exercises.ConcurrentMapExecutorDemo"
goto menu

:aggregation
echo.
echo === Running MapAggregationDemo ===
mvn compile exec:java -Dexec.mainClass="exercises.MapAggregationDemo"
goto menu

:cache
echo.
echo === Running DistributedCacheManager ===
mvn compile exec:java -Dexec.mainClass="projects.DistributedCacheManager"
goto menu

:aggregator
echo.
echo === Running ConcurrentDataAggregator ===
mvn compile exec:java -Dexec.mainClass="projects.ConcurrentDataAggregator"
goto menu

:all
echo.
echo === Running all demos ===
echo.
echo --- ConcurrentMapExecutorDemo ---
mvn compile exec:java -Dexec.mainClass="exercises.ConcurrentMapExecutorDemo"
echo.
echo --- MapAggregationDemo ---
mvn compile exec:java -Dexec.mainClass="exercises.MapAggregationDemo"
echo.
echo --- DistributedCacheManager ---
mvn compile exec:java -Dexec.mainClass="projects.DistributedCacheManager"
echo.
echo --- ConcurrentDataAggregator ---
mvn compile exec:java -Dexec.mainClass="projects.ConcurrentDataAggregator"
goto menu

:end
echo.
echo Goodbye!
pause

