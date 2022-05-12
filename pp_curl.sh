#!/bin/bash
UPLOAD_SOURCE=$(cat pp_body.painless | tr "\n" " " | sed 's/"/\\"/g')
echo "TRYING TO UPLOAD:"
echo ""
echo "$UPLOAD_SOURCE"
curl -X POST "localhost:9200/_scripts/sort" -H 'Content-Type: application/json' -d"
{
\"script\": {
\"lang\": \"painless\",
\"source\": \"$UPLOAD_SOURCE\"
}
}"
echo ""
echo "UPLOAD DONE"