#!/bin/bash

BASE_URL="${BASE_URL:-http://localhost:8080}"
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m'

echo "========================================"
echo "  Feed Normalizer - Manual Test Script  "
echo "========================================"
echo "  Target: $BASE_URL"
echo ""

run_test() {
    local description=$1
    local endpoint=$2
    local payload=$3

    echo "Testing: $description"
    STATUS=$(curl -s -o /dev/null -w "%{http_code}" -X POST "$BASE_URL$endpoint" \
        -H "Content-Type: application/json" \
        -d "$payload")

    if [ "$STATUS" -eq 200 ]; then
        echo -e "Result: ${GREEN}PASSED${NC} (HTTP $STATUS)"
    else
        echo -e "Result: ${RED}FAILED${NC} (HTTP $STATUS)"
    fi
    echo "----------------------------------------"
}

# ProviderAlpha - ODDS_CHANGE
run_test \
    "ProviderAlpha - ODDS_CHANGE" \
    "/provider-alpha/feed" \
    '{"msg_type":"odds_update","event_id":"ev123","values":{"1":2.0,"X":3.1,"2":3.8}}'

# ProviderAlpha - BET_SETTLEMENT
run_test \
    "ProviderAlpha - BET_SETTLEMENT" \
    "/provider-alpha/feed" \
    '{"msg_type":"settlement","event_id":"ev123","outcome":"1"}'

# ProviderBeta - ODDS_CHANGE
run_test \
    "ProviderBeta - ODDS_CHANGE" \
    "/provider-beta/feed" \
    '{"type":"ODDS","event_id":"ev456","odds":{"home":1.95,"draw":3.2,"away":4.0}}'

# ProviderBeta - BET_SETTLEMENT
run_test \
    "ProviderBeta - BET_SETTLEMENT" \
    "/provider-beta/feed" \
    '{"type":"SETTLEMENT","event_id":"ev456","result":"away"}'

echo ""
echo "========================================"
echo "  Check logs/feed-normalizer.log for   "
echo "  the normalized message output        "
echo "========================================"