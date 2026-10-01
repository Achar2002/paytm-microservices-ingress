# Kubernetes Commands for Paytm Microservices

## Prerequisites
Configure kubectl to connect to your EKS cluster:
```bash
aws eks update-kubeconfig --region us-east-1 --name kastro-eks
```

## Install NGINX Ingress Controller
```bash
# Method 1: Using the setup script
chmod +x setup/ingress-controller-setup.sh
./setup/ingress-controller-setup.sh

# Method 2: Manual installation
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.8.1/deploy/static/provider/aws/deploy.yaml

# Wait for ingress controller to be ready
kubectl wait --namespace ingress-nginx \
  --for=condition=ready pod \
  --selector=app.kubernetes.io/component=controller \
  --timeout=300s
```

## Deploy All Services
```bash
# Deploy all microservices
kubectl apply -f k8s/

# Or deploy individually
kubectl apply -f k8s/payment-gateway-deployment.yaml
kubectl apply -f k8s/wallet-service-deployment.yaml
kubectl apply -f k8s/transaction-service-deployment.yaml
kubectl apply -f k8s/user-service-deployment.yaml
kubectl apply -f k8s/profile-service-deployment.yaml
kubectl apply -f k8s/kyc-service-deployment.yaml
kubectl apply -f k8s/bank-service-deployment.yaml
kubectl apply -f k8s/loan-service-deployment.yaml
kubectl apply -f k8s/investment-service-deployment.yaml
kubectl apply -f k8s/email-service-deployment.yaml
kubectl apply -f k8s/sms-service-deployment.yaml
kubectl apply -f k8s/push-service-deployment.yaml
kubectl apply -f k8s/frontend-deployment.yaml
kubectl apply -f k8s/ingress.yaml
```

## Monitoring and Troubleshooting
```bash
# Check all pods
kubectl get pods

# Check services
kubectl get svc

# Check ingress
kubectl get ingress

# Check logs for a specific service
kubectl logs -f deployment/payment-gateway

# Check ingress controller logs
kubectl logs -f -n ingress-nginx deployment/ingress-nginx-controller

# Get external IP of ingress controller
kubectl get svc -n ingress-nginx
```

## Access the Application
After deployment, get the external IP/hostname:
```bash
kubectl get svc --namespace ingress-nginx
```

Access the application using the external IP or hostname provided by the load balancer.

## Cleanup
```bash
# Delete all resources
kubectl delete -f k8s/

# Delete ingress controller
kubectl delete -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.8.1/deploy/static/provider/aws/deploy.yaml
```