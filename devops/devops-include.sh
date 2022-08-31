#!/usr/bin/env bash

# Edit these to match your deployment/environment
export AWS_ACCOUNT=`cat ~/.secrets/aws-account`
export AWS_REGION="eu-west-2"
export BASE_DOMAIN="lintontt.com"
export SSL_CERTIFICATE_ARN=`cat ~/.secrets/aws-ssl-certificate`
export ECR_REGISTRY="${AWS_ACCOUNT}.dkr.ecr.${AWS_REGION}.amazonaws.com"
export K8S_CLUSTER_AWS_DEV="linton-development"
export K8S_CLUSTER_AWS_PROD="linton-production"

export MONGO_ATLAS_HOST=`cat ~/.secrets/mongo-atlas-hostname`
export MONGO_ATLAS_ADMIN_PASSWORD=`cat ~/.secrets/mongo-atlas-admin-password`
export MONGO_ATLAS_LINTON_PASSWORD=`cat ~/.secrets/mongo-atlas-linton-password`
export MONGO_ATLAS_URI=`echo "mongodb+srv://linton:${MONGO_ATLAS_LINTON_PASSWORD}@${MONGO_ATLAS_HOST}/?retryWrites=true&w=majority" | base64`

export MONGO_LOCAL_ADMIN_PASSWORD=`cat ~/.secrets/mongo-local-admin-password`
export MONGO_LOCAL_LINTON_PASSWORD=`cat ~/.secrets/mongo-local-linton-password`
export MONGO_LOCAL_URI=`echo "mongodb://linton:${MONGO_LOCAL_LINTON_PASSWORD}@host.docker.internal" | base64`


getServiceVersion() {
    SERVICE=$1
    echo `cat ../$SERVICE/version.properties | grep "version" | awk -F' *= *' '{print $2}'`
}

switchK8sContext() {
    OVERLAY=$1
    if [ "$OVERLAY" == "localk8s" ]; then
        KUBE_CONTEXT_ID=`kubectl config get-contexts | grep docker-desktop | sed 's/\*//' | awk '{print $1}'`
        kubectl config use-context $KUBE_CONTEXT_ID
    elif [ "$OVERLAY" == "aws-dev" ]; then
        aws eks update-kubeconfig --name $K8S_CLUSTER_AWS_DEV --region ${AWS_REGION}
    else
        aws eks update-kubeconfig --name $K8S_CLUSTER_AWS_PROD --region ${AWS_REGION}
    fi

    kubectl config get-contexts
}

dockerBuild() {
    SERVICE=$1
    SERVICE_DIR="../$SERVICE"

    # Build the JAR file
    $SERVICE_DIR/gradlew incrementSemanticVersion --patch -p $SERVICE_DIR
    $SERVICE_DIR/gradlew clean build -p $SERVICE_DIR
    SERVICE_VERSION=`cat $SERVICE_DIR/version.properties | grep "version" | awk -F' *= *' '{print $2}'`
    JAR_NAME=$SERVICE-${SERVICE_VERSION}-all.jar

    # Build Docker image
    echo "Building Docker image $SERVICE:$SERVICE_VERSION with JAR file $JAR_NAME"
    cp $SERVICE_DIR/build/libs/$JAR_NAME $SERVICE_DIR/docker/$JAR_NAME
    docker build -t $SERVICE:${SERVICE_VERSION} --build-arg "JAR_NAME=$JAR_NAME" $SERVICE_DIR/docker
    rm -rf $SERVICE_DIR/docker/*.jar

    # Sanity check
    docker images $SERVICE:$SERVICE_VERSION
}

dockerPublish() {
    SERVICE=$1
    SERVICE_VERSION=`getServiceVersion $SERVICE`

    # Publish to ECR
    docker images $SERVICE:$SERVICE_VERSION | grep $SERVICE_VERSION
    IMAGE_ID=`docker images $SERVICE:$SERVICE_VERSION | grep $SERVICE_VERSION | awk '{print $3}'`
    echo "Publishing Docker image: $IMAGE_ID to $ECR_REGISTRY"

    docker tag $IMAGE_ID $ECR_REGISTRY/$SERVICE:$SERVICE_VERSION
    docker images $ECR_REGISTRY/$SERVICE # Sanity check

    aws ecr get-login-password --region $AWS_REGION | docker login --username AWS --password-stdin $ECR_REGISTRY
    docker push $ECR_REGISTRY/$SERVICE:$SERVICE_VERSION
}

dockerBuildAndPublish() {
    SERVICE=$1
    dockerBuild $SERVICE
    dockerPublish $SERVICE
}

# Usage
# kubectlyApply SERVICE OVERLAY ENVIRONMENT_NAME
kubectlApply() {
    export SERVICE=$1
    export OVERLAY=$2
    export ENVIRONMENT_NAME=$3
    export SERVICE_VERSION=`getServiceVersion $SERVICE`
    switchK8sContext $OVERLAY

    if [ "$OVERLAY" == "localk8s" ]; then
        export DOCKER_IMAGE="${SERVICE}:${SERVICE_VERSION}"
    else
        export DOCKER_IMAGE="${ECR_REGISTRY}/${SERVICE}:${SERVICE_VERSION}"
    fi

    cat kubernetes/namespace.yaml | envsubst | kubectl apply -f -
    cat kubernetes/service-discovery.yaml | envsubst | kubectl apply -f -

    KUBE_CONFIG=`kubectl kustomize ../$SERVICE/kubernetes/overlays/$OVERLAY | envsubst`

    echo "----- Applying Kubernetes config -----"
    echo "$KUBE_CONFIG"
    echo "---------------------------------------"
    echo "$KUBE_CONFIG" | kubectl apply -f -
}

# Usage
# deployService SERVICE OVERLAY ENVIRONMENT_NAME
deployService() {
    export SERVICE=$1
    export OVERLAY=$2
    export ENVIRONMENT_NAME=$3

    dockerBuild $SERVICE
    if [ "$OVERLAY" != "localk8s" ]; then
        dockerPublish $SERVICE
    fi

    kubectlApply $SERVICE $OVERLAY $ENVIRONMENT_NAME
}
