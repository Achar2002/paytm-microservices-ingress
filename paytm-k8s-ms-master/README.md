# Paytm Clone Microservices Application

## Architecture Overview
This application consists of 12 microservices organized into 4 categories:

### 1. Payment Services (Port 9001-9003)
- **payment-gateway** (Port 9001) - Handles payment processing
- **wallet-service** (Port 9002) - Manages user wallets
- **transaction-service** (Port 9003) - Tracks all transactions

### 2. User Management (Port 9004-9006)
- **user-service** (Port 9004) - User registration and authentication
- **profile-service** (Port 9005) - User profile management
- **kyc-service** (Port 9006) - KYC verification

### 3. Financial Services (Port 9007-9009)
- **bank-service** (Port 9007) - Bank account management
- **loan-service** (Port 9008) - Loan processing
- **investment-service** (Port 9009) - Investment management

### 4. Notification Services (Port 9010-9012)
- **email-service** (Port 9010) - Email notifications
- **sms-service** (Port 9011) - SMS notifications
- **push-service** (Port 9012) - Push notifications

## Deployment
- AWS EKS Cluster: kastro-eks
- Region: us-east-1
- Jenkins CI/CD with parameterized builds
- Docker containers
- Ingress controller for external access