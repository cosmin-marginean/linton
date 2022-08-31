#!/usr/bin/env bash

set -e

ENVIRONMENT_NAME=production
source devops-include.sh

# Import MongoDB bootstrap data
(cd mongodb; echo $MONGO_ATLAS_ADMIN_PASSWORD | ./mongo-data-bootstrap.sh $MONGO_ATLAS_HOST linton-$ENVIRONMENT_NAME)

# Deploy all services
deployService linton-booking-api aws-prod $ENVIRONMENT_NAME
deployService linton-web aws-prod $ENVIRONMENT_NAME
