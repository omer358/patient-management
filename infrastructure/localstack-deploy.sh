#!/bin/bash

set -e  # Stop the script if any command fails

echo "🚀 Deploying CloudFormation stack to LocalStack..."

aws --endpoint-url=http://localhost:4566 cloudformation deploy \
    --stack-name patient-management \
    --template-file "./cdk.out/LocalStack.template.json"

echo "✅ Stack deployed. Fetching load balancer DNS..."

aws --endpoint-url=http://localhost:4566 elbv2 describe-load-balancers \
    --query "LoadBalancers[0].DNSName" \
    --output text
