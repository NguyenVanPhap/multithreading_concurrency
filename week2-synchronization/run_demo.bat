@echo off
echo ============================================
echo   Week 2 - Synchronization Demo
echo ============================================
echo.

:menu
echo Select demo to run:
echo.
echo [1] SyncDemo - Synchronized keyword
echo [2] LockDemo - ReentrantLock
echo [3] ReadWriteLockDemo - Read/Write locks
echo [4] BankSystem - Thread-safe banking
echo [5] MessageQueue - Producer-Consumer
echo [6] Run all
echo [0] Exit
echo.

set /p choice="Enter your choice: "

if "%choice%"=="1" goto sync
if "%choice%"=="2" goto lock
if "%choice%"=="3" goto rwlock
if "%choice%"=="4" goto bank
if "%choice%"=="5" goto queue
if "%choice%"=="6" goto all
if "%choice%"=="0" goto end

echo Invalid choice!
goto menu

:sync
echo.
echo === Running SyncDemo ===
mvn compile exec:java -Dexec.mainClass="exercises.SyncDemo"
goto menu

:lock
echo.
echo === Running LockDemo ===
mvn compile exec:java -Dexec.mainClass="exercises.LockDemo"
goto menu

:rwlock
echo.
echo === Running ReadWriteLockDemo ===
mvn compile exec:java -Dexec.mainClass="exercises.ReadWriteLockDemo"
goto menu

:bank
echo.
echo === Running BankSystem ===
mvn compile exec:java -Dexec.mainClass="projects.BankSystem"
goto menu

:queue
echo.
echo === Running MessageQueue ===
mvn compile exec:java -Dexec.mainClass="projects.MessageQueue"
goto menu

:all
echo.
echo === Running all demos ===
echo.
echo --- SyncDemo ---
mvn compile exec:java -Dexec.mainClass="exercises.SyncDemo"
echo.
echo --- LockDemo ---
mvn compile exec:java -Dexec.mainClass="exercises.LockDemo"
echo.
echo --- ReadWriteLockDemo ---
mvn compile exec:java -Dexec.mainClass="exercises.ReadWriteLockDemo"
echo.
echo --- BankSystem ---
mvn compile exec:java -Dexec.mainClass="projects.BankSystem"
echo.
echo --- MessageQueue ---
mvn compile exec:java -Dexec.mainClass="projects.MessageQueue"
goto menu

:end
echo.
echo Goodbye!
pause
