 #!/usr/bin/env bash

set -e

CLUSTER_NAME=$1

# Edit these before running this script
AWS_ACCOUNT=`cat ~/.secrets/aws-account`
AWS_CONSOLE_USER_ARN=`cat ~/.secrets/aws-console-user`
AWS_REGION="eu-west-2"
CLUSTER_ROLE="arn:aws:iam::${AWS_ACCOUNT}:role/EKS-ClusterRole"
LB_CONTROLLER_POLICY="arn:aws:iam::${AWS_ACCOUNT}:policy/AWSLoadBalancerControllerIAMPolicy"
ROUTE_53_POLICY="arn:aws:iam::${AWS_ACCOUNT}:policy/EKS-Route53"

eksctl create cluster \
       --name $CLUSTER_NAME \
       --node-type t3.medium \
       --nodes 2 \
       --nodes-min 2 \
       --nodes-max 2 \
       --region $AWS_REGION

eksctl create iamidentitymapping \
    --cluster $CLUSTER_NAME \
    --arn "$CLUSTER_ROLE" \
    --group eks-console-dashboard-full-access-group \
    --no-duplicate-arns

eksctl create iamidentitymapping \
    --cluster $CLUSTER_NAME \
    --arn "$AWS_CONSOLE_USER_ARN" \
    --group eks-console-dashboard-full-access-group,system:bootstrappers,system:nodes \
    --no-duplicate-arns

eksctl utils associate-iam-oidc-provider --cluster $CLUSTER_NAME --approve

eksctl create iamserviceaccount \
  --cluster=$CLUSTER_NAME \
  --namespace=kube-system \
  --name=aws-load-balancer-controller \
  --role-name "AmazonEKSLoadBalancerControllerRole" \
  --attach-policy-arn="$LB_CONTROLLER_POLICY" \
  --approve

eksctl create iamserviceaccount \
 --name external-dns \
 --cluster $CLUSTER_NAME \
 --attach-policy-arn "$ROUTE_53_POLICY" \
 --approve

helm repo add eks https://aws.github.io/eks-charts
helm repo update

helm install aws-load-balancer-controller eks/aws-load-balancer-controller \
  -n kube-system \
  --set clusterName=$CLUSTER_NAME \
  --set serviceAccount.create=false \
  --set serviceAccount.name=aws-load-balancer-controller

kubectl create clusterrolebinding service-reader-pod --clusterrole=service-reader --serviceaccount=default:default
kubectl apply -f cluster/external-dns.yaml
kubectl apply -f cluster/eks-console-full-access.yaml

# Setup CloudWatch: https://www.eksworkshop.com/intermediate/250_cloudwatch_container_insights/
STACK_NAME=$(eksctl get nodegroup --cluster $CLUSTER_NAME -o json | jq -r '.[].StackName')
ROLE_NAME=$(aws cloudformation describe-stack-resources --stack-name $STACK_NAME | jq -r '.StackResources[] | select(.ResourceType=="AWS::IAM::Role") | .PhysicalResourceId')

aws iam attach-role-policy \
  --role-name $ROLE_NAME \
  --policy-arn arn:aws:iam::aws:policy/CloudWatchAgentServerPolicy

curl -s https://raw.githubusercontent.com/aws-samples/amazon-cloudwatch-container-insights/latest/k8s-deployment-manifest-templates/deployment-mode/daemonset/container-insights-monitoring/quickstart/cwagent-fluentd-quickstart.yaml \
     | sed "s/{{cluster_name}}/$CLUSTER_NAME/;s/{{region_name}}/${AWS_REGION}/" | kubectl apply -f -

echo "Use the URL below to access Cloudwatch Container Insights in $AWS_REGION:"
echo "https://console.aws.amazon.com/cloudwatch/home?region=${AWS_REGION}#container-insights:performance/EKS:Service?~(query~(controls~(CW*3a*3aEKS.cluster~(~'$CLUSTER_NAME)))~context~())"
echo
