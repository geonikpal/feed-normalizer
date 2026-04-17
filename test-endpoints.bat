@echo off
setlocal

set BASE_URL=http://localhost:8080

echo ========================================
echo   Feed Normalizer - Manual Test Script
echo ========================================
echo   Target: %BASE_URL%
echo.

echo Waiting for service to start...
:wait
curl -s %BASE_URL%/actuator/health | find "UP" >nul
if errorlevel 1 (
    timeout /t 2 >nul
    goto wait
)
echo Service is up!
echo.

echo Testing: ProviderAlpha - ODDS_CHANGE
curl -s -o nul -w "HTTP %%{http_code}" -X POST %BASE_URL%/provider-alpha/feed ^
    -H "Content-Type: application/json" ^
    -d "{\"msg_type\":\"odds_update\",\"event_id\":\"ev123\",\"values\":{\"1\":2.0,\"X\":3.1,\"2\":3.8}}"
echo.
echo ----------------------------------------

echo Testing: ProviderAlpha - BET_SETTLEMENT
curl -s -o nul -w "HTTP %%{http_code}" -X POST %BASE_URL%/provider-alpha/feed ^
    -H "Content-Type: application/json" ^
    -d "{\"msg_type\":\"settlement\",\"event_id\":\"ev123\",\"outcome\":\"1\"}"
echo.
echo ----------------------------------------

echo Testing: ProviderBeta - ODDS_CHANGE
curl -s -o nul -w "HTTP %%{http_code}" -X POST %BASE_URL%/provider-beta/feed ^
    -H "Content-Type: application/json" ^
    -d "{\"type\":\"ODDS\",\"event_id\":\"ev456\",\"odds\":{\"home\":1.95,\"draw\":3.2,\"away\":4.0}}"
echo.
echo ----------------------------------------

echo Testing: ProviderBeta - BET_SETTLEMENT
curl -s -o nul -w "HTTP %%{http_code}" -X POST %BASE_URL%/provider-beta/feed ^
    -H "Content-Type: application/json" ^
    -d "{\"type\":\"SETTLEMENT\",\"event_id\":\"ev456\",\"result\":\"away\"}"
echo.
echo ----------------------------------------

echo.
echo ========================================
echo   Check logs/feed-normalizer.log for
echo   the normalized message output
echo ========================================
pause