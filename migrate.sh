TIMESTAMP=$(date +"%Y%m%d%H%M%S")

DIR="src/main/resources/db/migration"
FILENAME="V${TIMESTAMP}__$1.sql"

touch "$DIR/$FILENAME"
echo "Migration created: $DIR/$FILENAME"
