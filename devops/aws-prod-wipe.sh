#!/usr/bin/env bash

set -e

source devops-include.sh
switchK8sContext aws-prod
kubectl delete namespace production
