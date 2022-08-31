#!/usr/bin/env bash

set -e

if [[ $# -lt 1 ]] ; then
    echo "This script requires 1 argument: <environment-name>"
    exit 1
fi

ENVIRONMENT_NAME=$1
source devops-include.sh

# Import MongoDB bootstrap data
(cd mongodb; echo $MONGO_ATLAS_ADMIN_PASSWORD | ./mongo-data-bootstrap.sh $MONGO_ATLAS_HOST linton-$ENVIRONMENT_NAME)

# Deploy all services
deployService linton-booking-api aws-dev $ENVIRONMENT_NAME
deployService linton-web aws-dev $ENVIRONMENT_NAME
