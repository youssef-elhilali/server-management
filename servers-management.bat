@echo off

echo Building Spring Boot app...

call mvn package -DskipTests

call mvn clean install -DskipTests

if %errorlevel% neq 0 goto error

echo Loading environment variables...

set ENV_FILE=.dev.env

if exist %ENV_FILE% (
   for /f "delims=" %%a in (%ENV_FILE%) do (
      set %%a
   )
)

echo Starting containers...

docker-compose up -d --build