@echo off
echo ============================================
echo   Week 3 - Deadlock và Advanced Locks Demo
echo ============================================
echo.

:menu
echo Select demo to run:
echo.
echo [1] DeadlockDemo - Deadlock detection và prevention
echo [2] LockOrderingDemo - Lock ordering để tránh deadlock
echo [3] TimeoutLockDemo - Timeout locks và deadlock detection
echo [4] DiningPhilosophers - Classic deadlock problem
echo [5] ResourceManager - Resource management với deadlock prevention
echo [6] Run all
echo [0] Exit
echo.

set /p choice="Enter your choice: "

if "%choice%"=="1" goto deadlock
if "%choice%"=="2" goto ordering
if "%choice%"=="3" goto timeout
if "%choice%"=="4" goto philosophers
if "%choice%"=="5" goto resource
if "%choice%"=="6" goto all
if "%choice%"=="0" goto end

echo Invalid choice!
goto menu

:deadlock
echo.
echo === Running DeadlockDemo ===
mvn compile exec:java -Dexec.mainClass="exercises.DeadlockDemo"
goto menu

:ordering
echo.
echo === Running LockOrderingDemo ===
mvn compile exec:java -Dexec.mainClass="exercises.LockOrderingDemo"
goto menu



:philosophers
echo.
echo === Running DiningPhilosophers ===
mvn compile exec:java -Dexec.mainClass="projects.DiningPhilosophers"
goto menu

:resource
echo.
echo === Running ResourceManager ===
mvn compile exec:java -Dexec.mainClass="projects.ResourceManager"
goto menu

:all
echo.
echo === Running all demos ===
echo.
echo --- DeadlockDemo ---
mvn compile exec:java -Dexec.mainClass="exercises.DeadlockDemo"
echo.
echo --- LockOrderingDemo ---
mvn compile exec:java -Dexec.mainClass="exercises.LockOrderingDemo"
echo.
echo --- TimeoutLockDemo ---
mvn compile exec:java -Dexec.mainClass="exercises.TimeoutLockDemo"
echo.
echo --- DiningPhilosophers ---
mvn compile exec:java -Dexec.mainClass="projects.DiningPhilosophers"
echo.
echo --- ResourceManager ---
mvn compile exec:java -Dexec.mainClass="projects.ResourceManager"
goto menu

:end
echo.
echo Goodbye!
pause

