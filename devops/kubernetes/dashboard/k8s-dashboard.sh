#!/usr/bin/env bash

kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.5.0/aio/deploy/recommended.yaml
kubectl apply -f dashboard-role.yaml
kubectl apply -f dashboard-adminuser.yaml
