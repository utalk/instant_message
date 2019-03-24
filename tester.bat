@echo off
echo Initialing programs
set num=9
echo The number of applications is %num%
for /L %%i in (1,1,%num%) do start /b gradle run --args '%%i'

