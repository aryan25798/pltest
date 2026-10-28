@echo off
echo ========================================================
echo Running ProjectLens Test Automation Suite
echo ========================================================
mvn test
echo ========================================================
echo Execution Completed!
echo Extent Report generated at: test-output\ExecutionReport.html
echo ========================================================
pause
