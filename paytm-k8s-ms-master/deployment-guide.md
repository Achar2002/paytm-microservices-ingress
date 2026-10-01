# Paytm Microservices Deployment Guide

## Overview
This guide helps you deploy the Paytm Clone microservices application on AWS EKS using Jenkins CI/CD.

## Prerequisites
- AWS EKS cluster named `kastro-eks` in `us-east-1` region
- Jenkins server with required plugins
- Docker Hub account (username: `kastrov`)
- AWS credentials configured in Jenkins as `aws-credentials`
- Docker Hub credentials configured in Jenkins as `dockerhub-credentials`

## Jenkins Setup

### 1. Required Jenkins Plugins
- AWS Steps Plugin
- Docker Pipeline Plugin
- Kubernetes Plugin
- Pipeline Plugin

### 2. Jenkins Credentials
- `aws-credentials`: AWS Access Key and Secret Key
- `dockerhub-credentials`: Docker Hub username and password

### 3. Create Jenkins Jobs
Create individual Jenkins jobs for each microservice:
- `payment-gateway-pipeline`
- `wallet-service-pipeline`
- `transaction-service-pipeline`
- `user-service-pipeline`
- `profile-service-pipeline`
- `kyc-service-pipeline`
- `bank-service-pipeline`
- `loan-service-pipeline`
- `investment-service-pipeline`
- `email-service-pipeline`
- `sms-service-pipeline`
- `push-service-pipeline`

## Deployment Steps

### 1. Install NGINX Ingress Controller
```bash
# Connect to EKS cluster
aws eks update-kubeconfig --region us-east-1 --name kastro-eks

# Install NGINX Ingress Controller
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.8.1/deploy/static/provider/aws/deploy.yaml

# Wait for controller to be ready
kubectl wait --namespace ingress-nginx \
  --for=condition=ready pod \
  --selector=app.kubernetes.io/component=controller \
  --timeout=300s

# Get external IP
kubectl get svc --namespace ingress-nginx
```

### 2. Deploy Using Jenkins
1. Run the main Jenkins pipeline with parameters:
   - SERVICE: `all` (to deploy all services)
   - BUILD_NUMBER: `1`
   - ENVIRONMENT: `dev`

2. Or deploy individual services by selecting specific service names

### 3. Access the Application
After deployment, access the application using the external IP/hostname from the ingress controller.

## Service Architecture

### Payment Services (Ports 9001-9003)
- **Payment Gateway** (9001): Handles payment processing
- **Wallet Service** (9002): Manages digital wallets
- **Transaction Service** (9003): Transaction tracking

### User Management (Ports 9004-9006)
- **User Service** (9004): User registration and authentication
- **Profile Service** (9005): User profile management
- **KYC Service** (9006): Know Your Customer verification

### Financial Services (Ports 9007-9009)
- **Bank Service** (9007): Bank account management
- **Loan Service** (9008): Loan processing
- **Investment Service** (9009): Investment management

### Notification Services (Ports 9010-9012)
- **Email Service** (9010): Email notifications
- **SMS Service** (9011): SMS notifications
- **Push Service** (9012): Push notifications

## Monitoring

### Health Checks
Each service provides health check endpoints:
- `GET /{service-name}/health`

### Kubernetes Monitoring
```bash
# Check pods
kubectl get pods

# Check services
kubectl get svc

# Check ingress
kubectl get ingress

# View logs
kubectl logs -f deployment/{service-name}
```

## Troubleshooting

### Common Issues
1. **Service not starting**: Check logs using `kubectl logs`
2. **Ingress not working**: Verify ingress controller is running
3. **Build failures**: Check Jenkins logs and credentials

### Useful Commands
```bash
# Scale deployment
kubectl scale deployment {service-name} --replicas=3

# Update deployment
kubectl rollout restart deployment/{service-name}

# Check deployment status
kubectl rollout status deployment/{service-name}
```

## Security Considerations
- All services run on non-root ports (9001-9012)
- Services communicate internally via ClusterIP
- External access only through ingress controller
- Use proper RBAC for Jenkins service account

## Scaling
- Each service is configured with 2 replicas by default
- Can be scaled horizontally based on load
- Use HPA (Horizontal Pod Autoscaler) for automatic scaling

## Backup and Recovery
- Use EKS snapshots for cluster backup
- Docker images stored in Docker Hub for versioning
- Configuration stored in Git repository