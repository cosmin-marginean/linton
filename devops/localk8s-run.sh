#!/usr/bin/env bash

set -e
source devops-include.sh

# Import MongoDB bootstrap data
(cd mongodb; echo $MONGO_LOCAL_ADMIN_PASSWORD | ./mongo-data-bootstrap.sh localhost linton-localk8s)

# Deploy all services
deployService linton-booking-api localk8s localk8s
deployService linton-web localk8s localk8s
