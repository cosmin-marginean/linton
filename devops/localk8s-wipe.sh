#!/usr/bin/env bash

set -e
source devops-include.sh

switchK8sContext localk8s
kubectl delete namespace localk8s
