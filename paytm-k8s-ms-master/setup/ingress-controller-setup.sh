#!/bin/bash

# Install NGINX Ingress Controller on AWS EKS
echo "Installing NGINX Ingress Controller..."

# Add NGINX Ingress Controller
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.8.1/deploy/static/provider/aws/deploy.yaml

# Wait for the ingress controller to be ready
echo "Waiting for NGINX Ingress Controller to be ready..."
kubectl wait --namespace ingress-nginx \
  --for=condition=ready pod \
  --selector=app.kubernetes.io/component=controller \
  --timeout=300s

# Get the external IP/hostname
echo "Getting the external load balancer URL..."
kubectl get svc --namespace ingress-nginx

echo "NGINX Ingress Controller installation completed!"
echo "Please note the EXTERNAL-IP or hostname from the above output."
echo "You can access your application using this URL once all services are deployed."