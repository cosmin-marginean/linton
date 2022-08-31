#!/usr/bin/env bash

set -e

if [[ $# -lt 1 ]] ; then
    echo "This script requires 1 argument: <environment-name>"
    exit 1
fi

source devops-include.sh
switchK8sContext aws-dev
kubectl delete namespace $1
