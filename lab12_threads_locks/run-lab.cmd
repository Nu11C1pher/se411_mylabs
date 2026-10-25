@echo off
setlocal
cd /d "%~dp0"
if not exist target\classes mkdir target\classes
if not exist target\test-classes mkdir target\test-classes
dir /s /b src\main\java\*.java > target\sources.txt
javac --release 17 -d target\classes @target\sources.txt
if errorlevel 1 goto failed
javac --release 17 -cp target\classes -d target\test-classes src\test\java\LockChecks.java
if errorlevel 1 goto failed
java -cp "target\classes;target\test-classes" edu.psu.se411.lab12_threads.LockChecks
if errorlevel 1 goto failed
for %%A in (1000 10) do (
  for %%M in (App CachedApp) do (
    java -cp target\classes edu.psu.se411.lab12_threads.%%M %%A 1000000
    if errorlevel 1 goto failed
  )
)
echo All checks passed.
pause
exit /b 0
:failed
echo Lab check failed. See the error above.
pause
exit /b 1
