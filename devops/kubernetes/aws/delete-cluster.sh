 #!/usr/bin/env bash

set -e

CLUSTER_NAME=$1
AWS_REGION="eu-west-2"

STACK_NAME=$(eksctl get nodegroup --cluster $CLUSTER_NAME -o json | jq -r '.[].StackName')
ROLE_NAME=$(aws cloudformation describe-stack-resources --stack-name $STACK_NAME | jq -r '.StackResources[] | select(.ResourceType=="AWS::IAM::Role") | .PhysicalResourceId')
aws iam detach-role-policy \
  --policy-arn arn:aws:iam::aws:policy/CloudWatchAgentServerPolicy \
  --role-name ${ROLE_NAME}

eksctl delete cluster --name $CLUSTER_NAME --region $AWS_REGION
