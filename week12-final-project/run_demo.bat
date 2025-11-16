@echo off
echo ============================================
echo   Week 12 - Final Project Demo
echo ============================================
echo.

echo Running Final Project...
mvn compile exec:java -Dexec.mainClass="project.TaskProcessor"

pause

