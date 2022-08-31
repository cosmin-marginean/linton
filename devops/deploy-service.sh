#!/usr/bin/env bash

set -e

if [[ $# -lt 3 ]] ; then
    echo "This script requires 3 arguments: <service> <overlay> <environment>"
    exit 1
fi

source devops-include.sh
deployService $1 $2 $3
