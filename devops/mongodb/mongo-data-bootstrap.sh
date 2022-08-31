#!/usr/bin/env bash

set -e

if [[ $# -lt 2 ]] ; then
    echo "Please specify MongoDB hostname and database:"
    echo "./mongo-data-bootstrap.sh <hostname> <database>"
    exit 1
fi

MONGO_HOST="$1"
MONGO_DATABASE="$2"
MONGO_USER="admin"

echo -n "MongoDB password for user $MONGO_USER@$MONGO_HOST: "
read -s MONGO_PASSWORD
echo

if [[ $MONGO_HOST == *"mongodb.net"* ]]; then
    PROTOCOL="mongodb+srv"
else
    PROTOCOL="mongodb"
fi

MONGO_URI="$PROTOCOL://$MONGO_HOST/$MONGO_DATABASE"
echo "Importing bootstrap data to $MONGO_URI and database $MONGO_DATABASE"

mongosh $MONGO_URI -u $MONGO_USER -p $MONGO_PASSWORD --authenticationDatabase "admin" \
        --eval "db.dropDatabase()"

mongoimport --uri $MONGO_URI -u $MONGO_USER -p $MONGO_PASSWORD --authenticationDatabase "admin" \
            --collection users --file bootstrap-data/staff.json --jsonArray

mongoimport --uri $MONGO_URI -u $MONGO_USER -p $MONGO_PASSWORD --authenticationDatabase "admin" \
            --collection users --file bootstrap-data/guests.json --jsonArray

mongoimport --uri $MONGO_URI -u $MONGO_USER -p $MONGO_PASSWORD --authenticationDatabase "admin" \
            --collection bookings --file bootstrap-data/bookings.json --jsonArray

mongoimport --uri $MONGO_URI -u $MONGO_USER -p $MONGO_PASSWORD --authenticationDatabase "admin" \
            --collection passwords --file bootstrap-data/passwords.json --jsonArray
