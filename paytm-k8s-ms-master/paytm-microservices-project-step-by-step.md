# Paytm-Style Microservices Project — Step-by-Step Deployment Guide

> This document is a practical implementation guide based on the project notes. It keeps the project flow and required commands/configuration, but removes the Jenkins pipeline code and exposed credentials/tokens.

---

## Step 1 — Basic AWS Setup

### 1.1 Launch the VM

Launch one Ubuntu VM for the project setup.

- OS: Ubuntu 24.04
- Instance type used in the original notes: `t2.large`
- Storage: 28 GB
- Name: `BMS-Server`

Configure the Security Group with the ports required by the project.

| Port | Protocol | Purpose |
|---|---|---|
| 22 | TCP | SSH access |
| 443 | TCP | HTTPS |
| 6443 | TCP | Kubernetes API server |
| 465 | TCP | SMTPS / secure email |
| 25 | TCP | SMTP |

> Only expose ports that are actually required for your environment. Avoid opening administrative ports to `0.0.0.0/0` when a restricted source can be used.

---

## Step 2 — Create the EKS Cluster

### 2.1 Create an IAM user

Create a dedicated IAM identity for the EKS setup instead of using the AWS root account.

The original project notes use permissions covering:

- EC2
- EKS
- EKS CNI
- EKS Cluster
- EKS Worker Nodes
- CloudFormation
- IAM
- VPC

Create access keys for the identity if your lab setup requires access-key authentication.

**Do not put access keys, secret keys, or downloaded credential files into GitHub.**

### 2.2 Connect to the VM

```bash
sudo apt update
```

### 2.3 Install AWS CLI

```bash
curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
sudo apt install unzip
unzip awscliv2.zip
sudo ./aws/install
```

Verify:

```bash
aws --version
```

Configure AWS CLI:

```bash
aws configure
```

Provide the credentials, region, and output format required by your AWS account.

The original notes use:

```text
Region: us-east-1
Output: json
```

### 2.4 Install kubectl

The original notes use the following kubectl installation:

```bash
curl -o kubectl https://amazon-eks.s3.us-west-2.amazonaws.com/1.19.6/2021-01-05/bin/linux/amd64/kubectl
chmod +x ./kubectl
sudo mv ./kubectl /usr/local/bin
```

Verify:

```bash
kubectl version --short --client
```

> The project notes use an older kubectl binary. For a new cluster, use a kubectl version compatible with the Kubernetes version of your EKS cluster.

### 2.5 Install eksctl

```bash
curl --silent --location "https://github.com/weaveworks/eksctl/releases/latest/download/eksctl_$(uname -s)_amd64.tar.gz" | tar xz -C /tmp
sudo mv /tmp/eksctl /usr/local/bin
```

Verify:

```bash
eksctl version
```

### 2.6 Create the EKS cluster

The original project uses:

```bash
eksctl create cluster --name kastro-cluster --region us-east-1 --node-type t2.medium --zones us-east-1a,us-east-1b
```

Verify the cluster:

```bash
kubectl get nodes
kubectl get pods -A
```

---

## Step 3 — Install Jenkins

### 3.1 Connect to the Jenkins server

Install Java first:

```bash
sudo apt install openjdk-17-jre-headless -y
```

Add the Jenkins repository:

```bash
sudo wget -O /usr/share/keyrings/jenkins-keyring.asc \
  https://pkg.jenkins.io/debian-stable/jenkins.io-2023.key

echo deb [signed-by=/usr/share/keyrings/jenkins-keyring.asc] \
  https://pkg.jenkins.io/debian-stable binary/ | sudo tee \
  /etc/apt/sources.list.d/jenkins.list > /dev/null
```

Install Jenkins:

```bash
sudo apt-get update
sudo apt-get install jenkins -y
```

Enable/start Jenkins and verify it:

```bash
sudo systemctl enable jenkins
sudo systemctl start jenkins
sudo systemctl status jenkins
```

Open port `8080` in the Jenkins server Security Group.

Access:

```text
http://<jenkins-server-ip>:8080
```

Complete the initial Jenkins setup.

---

## Step 4 — Install Docker

### 4.1 Install Docker dependencies

```bash
sudo apt-get update
sudo apt-get install -y ca-certificates curl
```

### 4.2 Add Docker repository

```bash
sudo install -m 0755 -d /etc/apt/keyrings
sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
sudo chmod a+r /etc/apt/keyrings/docker.asc
```

```bash
echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu \
$(. /etc/os-release && echo "$VERSION_CODENAME") stable" | \
sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
```

Install Docker:

```bash
sudo apt-get update
sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
```

Verify:

```bash
docker --version
```

Test Docker:

```bash
docker pull hello-world
docker run hello-world
```

### 4.3 Allow Jenkins to use Docker

```bash
sudo usermod -aG docker jenkins
sudo systemctl restart docker
sudo systemctl restart jenkins
```

Install Maven:

```bash
sudo apt install maven -y
```

Verify:

```bash
mvn -version
```

### 4.4 Configure Docker Hub

Log in from the Jenkins server when required:

```bash
docker login
```

Use your own Docker Hub username and password/token. Do not store the password/token in this document or GitHub.

---

## Step 5 — Install SonarQube

Run SonarQube on the Jenkins server:

```bash
docker run -d --name sonar -p 9000:9000 sonarqube:lts-community
```

Verify:

```bash
docker images
docker ps
```

Open port `9000` in the Security Group.

Access:

```text
http://<jenkins-server-ip>:9000
```

Complete the initial SonarQube setup and create a new token when Jenkins integration requires one.

**Store the token only in Jenkins credentials. Never commit it to GitHub.**

---

## Step 6 — Configure Jenkins

Open Jenkins:

```text
http://<jenkins-server-ip>:8080
```

### 6.1 Install required plugins

Install the plugins used by the project, including:

- SonarQube Scanner
- Docker
- Docker Commons
- Docker Pipeline
- Docker API
- Docker Build Step
- AWS Credentials
- Pipeline Stage View
- Email Extension Template
- Kubernetes
- Kubernetes Client API
- Kubernetes Credentials
- Kubernetes CLI
- Kubernetes Credentials Provider
- Config File Provider
- Prometheus Metrics

### 6.2 Configure SonarQube

In Jenkins, configure the SonarQube server and store the SonarQube token as a Jenkins credential.

### 6.3 Configure Docker Hub credentials

Create a Jenkins credential:

```text
Kind: Username with Password
ID: dockerhub-credentials
Username: <your-dockerhub-username>
Password: <your-dockerhub-token>
```

### 6.4 Configure AWS credentials

Create an AWS credential in Jenkins using your approved AWS authentication method.

The project notes refer to the credential ID:

```text
aws-credentials
```

Do not put actual access keys in this document.

### 6.5 Configure Docker in Jenkins

Go to:

```text
Manage Jenkins → System Configuration → Tools
```

Configure Docker installation as required by the Jenkins environment.

---

## Step 7 — Configure Email Notifications

Create an email application password/token through your email provider if required.

Store it in Jenkins credentials instead of putting it in the project files.

In Jenkins configure the SMTP settings.

For the Gmail settings used in the original notes:

```text
SMTP Server: smtp.gmail.com
SMTP Port: 465
SSL: Enabled
```

Configure both the Extended Email Notification and Email Notification sections as required.

Send a test email and verify that it is received.

Configure build notification triggers for events such as:

- Success
- Failure
- Always

---

## Step 8 — Prepare the Kubernetes Application

Create a custom namespace for the application:

```bash
kubectl create namespace paytm-app
```

Verify:

```bash
kubectl get namespaces
```

The application contains multiple microservices. The services in the original project notes are:

| Microservice | Port |
|---|---:|
| payment-gateway | 9001 |
| wallet-service | 9002 |
| transaction-service | 9003 |
| user-service | 9004 |
| profile-service | 9005 |
| kyc-service | 9006 |
| bank-service | 9007 |
| loan-service | 9008 |
| investment-service | 9009 |
| email-service | 9010 |
| sms-service | 9011 |
| push-service | 9012 |

Each microservice is containerized separately and has its own Kubernetes deployment/service configuration.

---

## Step 9 — Build and Push Microservice Images

For each microservice:

1. Enter the microservice directory.
2. Build the application.
3. Create its Docker image.
4. Tag the image with your Docker Hub repository.
5. Push the image to Docker Hub.

Example flow:

```bash
cd <service-directory>
docker build -t <dockerhub-username>/<service-name>:<tag> .
docker push <dockerhub-username>/<service-name>:<tag>
```

Repeat for every required microservice.

Verify images:

```bash
docker images
```

---

## Step 10 — Deploy Microservices to Kubernetes

Create/update the Kubernetes YAML files for each microservice.

Each microservice deployment should define the required:

- Deployment
- Container image
- Container port
- Replica count
- Environment variables, if required
- Resource configuration, if required

Create a Kubernetes Service for each microservice so that other Kubernetes components can reach it.

Apply the deployment files:

```bash
kubectl apply -f <service-deployment.yaml> -n paytm-app
```

Apply the service files:

```bash
kubectl apply -f <service-service.yaml> -n paytm-app
```

Verify:

```bash
kubectl get deployments -n paytm-app
kubectl get pods -n paytm-app
kubectl get svc -n paytm-app
```

For troubleshooting:

```bash
kubectl describe pod <pod-name> -n paytm-app
kubectl logs <pod-name> -n paytm-app
```

---

## Step 11 — Deploy the Frontend

Create the frontend Kubernetes deployment and service configuration.

Apply the frontend deployment:

```bash
kubectl apply -f k8s/frontend-deployment.yaml -n paytm-app
```

Verify:

```bash
kubectl get pods -n paytm-app
kubectl get svc -n paytm-app
```

---

## Step 12 — Install NGINX Ingress Controller

Install the NGINX Ingress Controller used in the original project notes:

```bash
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.8.1/deploy/static/provider/aws/deploy.yaml
```

Check the controller pods:

```bash
kubectl get pods -n ingress-nginx --watch
```

Then:

```bash
kubectl get pods -n ingress-nginx
kubectl get svc -n ingress-nginx
```

Get the Load Balancer hostname:

```bash
kubectl get svc -n ingress-nginx ingress-nginx-controller -o jsonpath='{.status.loadBalancer.ingress[0].hostname}'
```

Save the returned hostname for testing the application.

---

## Step 13 — Configure Kubernetes Ingress

Create the application's Ingress YAML file.

The Ingress defines how incoming requests are routed to the appropriate Kubernetes Services.

Apply it:

```bash
kubectl apply -f k8s/ingress.yaml -n paytm-app
```

Verify:

```bash
kubectl get ingress -n paytm-app
kubectl describe ingress <ingress-name> -n paytm-app
```

Application flow:

```text
User
  ↓
AWS Load Balancer / Ingress Controller
  ↓
Kubernetes Ingress
  ↓
Kubernetes Service
  ↓
Microservice Pod
```

---

## Step 14 — Verify the Application

Check all application resources:

```bash
kubectl get all -n paytm-app
```

Check pods:

```bash
kubectl get pods -n paytm-app
```

Check services:

```bash
kubectl get svc -n paytm-app
```

Check ingress:

```bash
kubectl get ingress -n paytm-app
```

Open the Ingress Load Balancer hostname in the browser and verify the application.

---

# Monitoring Setup

## Step 15 — Create Monitoring Server

Launch a separate Ubuntu VM for monitoring.

The original notes use:

- Ubuntu 22.04
- Instance type: `t2.medium`
- Name: `Monitoring Server`

Open the monitoring ports required by the setup, including:

```text
9090 — Prometheus
9100 — Node Exporter
3000 — Grafana
```

---

## Step 16 — Install Prometheus

Connect to the Monitoring Server.

### 16.1 Create Prometheus system user

```bash
sudo apt update
sudo useradd --system --no-create-home --shell /bin/false prometheus
```

### 16.2 Download Prometheus

The original notes use Prometheus `2.47.1`:

```bash
sudo wget https://github.com/prometheus/prometheus/releases/download/v2.47.1/prometheus-2.47.1.linux-amd64.tar.gz
tar -xvf prometheus-2.47.1.linux-amd64.tar.gz
sudo mkdir -p /data /etc/prometheus
cd prometheus-2.47.1.linux-amd64/
```

Move the binaries:

```bash
sudo mv prometheus promtool /usr/local/bin/
```

Move configuration files:

```bash
sudo mv consoles/ console_libraries/ /etc/prometheus/
sudo mv prometheus.yml /etc/prometheus/prometheus.yml
```

Set ownership:

```bash
sudo chown -R prometheus:prometheus /etc/prometheus/ /data/
```

Clean up:

```bash
cd
rm -rf prometheus-2.47.1.linux-amd64.tar.gz
```

Verify:

```bash
prometheus --version
prometheus --help
```

### 16.3 Create Prometheus systemd service

Create:

```bash
sudo vi /etc/systemd/system/prometheus.service
```

Use the Prometheus service configuration from the project setup, pointing to:

```text
Config: /etc/prometheus/prometheus.yml
Data: /data
Port: 9090
```

Enable and start:

```bash
sudo systemctl enable prometheus
sudo systemctl start prometheus
sudo systemctl status prometheus
```

Open port `9090` and access:

```text
http://<monitoring-server-ip>:9090
```

Go to:

```text
Status → Targets
```

Prometheus itself should appear as an available target.

---

## Step 17 — Install Node Exporter

Create the Node Exporter user:

```bash
sudo useradd --system --no-create-home --shell /bin/false node_exporter
```

Download Node Exporter:

```bash
wget https://github.com/prometheus/node_exporter/releases/download/v1.6.1/node_exporter-1.6.1.linux-amd64.tar.gz
```

Extract and install:

```bash
tar -xvf node_exporter-1.6.1.linux-amd64.tar.gz
sudo mv node_exporter-1.6.1.linux-amd64/node_exporter /usr/local/bin/
rm -rf node_exporter*
```

Verify:

```bash
node_exporter --version
```

Create the systemd service:

```bash
sudo vi /etc/systemd/system/node_exporter.service
```

Configure Node Exporter to run on port `9100`.

Enable and start:

```bash
sudo systemctl enable node_exporter
sudo systemctl start node_exporter
sudo systemctl status node_exporter
```

Open port `9100` in the Monitoring Server Security Group.

---

## Step 18 — Configure Prometheus to Scrape Metrics

Edit:

```bash
sudo vi /etc/prometheus/prometheus.yml
```

Add scrape jobs for Node Exporter and Jenkins.

Conceptually:

```yaml
scrape_configs:
  - job_name: "prometheus"
    static_configs:
      - targets: ["localhost:9090"]

  - job_name: "node_exporter"
    static_configs:
      - targets: ["<monitoring-server-ip>:9100"]

  - job_name: "jenkins"
    metrics_path: "/prometheus"
    static_configs:
      - targets: ["<jenkins-server-ip>:<jenkins-port>"]
```

Replace the placeholder IP addresses with your own values.

Validate the configuration:

```bash
promtool check config /etc/prometheus/prometheus.yml
```

The configuration should return `SUCCESS`.

Reload Prometheus:

```bash
curl -X POST http://localhost:9090/-/reload
```

Open:

```text
http://<monitoring-server-ip>:9090/targets
```

Expected targets include:

```text
Prometheus
node_exporter
Jenkins
```

---

# Grafana Setup

## Step 19 — Install Grafana

Run these commands on the Monitoring Server.

### 19.1 Install dependencies

```bash
sudo apt-get update
sudo apt-get install -y apt-transport-https software-properties-common
```

### 19.2 Add the Grafana key

```bash
wget -q -O - https://packages.grafana.com/gpg.key | sudo apt-key add -
```

### 19.3 Add Grafana repository

```bash
echo "deb https://packages.grafana.com/oss/deb stable main" | sudo tee -a /etc/apt/sources.list.d/grafana.list
```

### 19.4 Install Grafana

```bash
sudo apt-get update
sudo apt-get -y install grafana
```

### 19.5 Start Grafana

```bash
sudo systemctl enable grafana-server
sudo systemctl start grafana-server
sudo systemctl status grafana-server
```

### 19.6 Access Grafana

Open port `3000`.

Access:

```text
http://<monitoring-server-ip>:3000
```

Log in and complete the initial Grafana setup.

---

## Step 20 — Add Prometheus as Grafana Data Source

In Grafana:

```text
Connections → Data Sources → Prometheus
```

Set the Prometheus URL:

```text
http://<monitoring-server-ip>:9090
```

Save and test the connection.

The data source should connect successfully.

---

## Step 21 — Import Monitoring Dashboards

### 21.1 Node Exporter dashboard

In Grafana:

```text
Dashboards → Import
```

Use the Node Exporter dashboard referenced by the original project notes.

Select the Prometheus data source and import the dashboard.

### 21.2 Jenkins dashboard

Import the Jenkins Performance and Health Overview dashboard referenced in the original notes.

Select Prometheus as the data source and import the dashboard.

After importing, check:

```text
Dashboards
```

Verify that the Node Exporter and Jenkins dashboards are available.

---

# Argo CD / GitOps Setup

## Step 22 — Install Helm

```bash
curl -fsSL -o get_helm.sh https://raw.githubusercontent.com/helm/helm/main/scripts/get-helm-3
chmod 700 get_helm.sh
./get_helm.sh
```

Verify:

```bash
helm version
```

---

## Step 23 — Install Argo CD

Add the Argo Helm repository:

```bash
helm repo add argo https://argoproj.github.io/argo-helm
helm repo update
```

Create the namespace:

```bash
kubectl create namespace argocd
```

Install Argo CD:

```bash
helm install argocd argo/argo-cd --namespace argocd
```

Verify:

```bash
kubectl get all -n argocd
```

If Argo CD pods remain Pending, check pod events and available worker-node resources:

```bash
kubectl describe pod <pod-name> -n argocd
kubectl get nodes
```

---

## Step 24 — Expose Argo CD

Change the Argo CD server service to a LoadBalancer:

```bash
kubectl patch svc argocd-server -n argocd -p '{"spec": {"type": "LoadBalancer"}}'
```

Check the service:

```bash
kubectl get svc argocd-server -n argocd
```

If required, install `jq`:

```bash
yum install jq -y
```

Get the Load Balancer hostname:

```bash
kubectl get svc argocd-server -n argocd -o json | jq --raw-output '.status.loadBalancer.ingress[0].hostname'
```

Open the returned hostname in the browser.

---

## Step 25 — Get the Argo CD Admin Password

Run:

```bash
kubectl -n argocd get secret argocd-initial-admin-secret -o jsonpath="{.data.password}" | base64 -d
```

Use:

```text
Username: admin
Password: <value returned by the command>
```

Do not store the password in GitHub.

---

## Step 26 — Create the Argo CD Application

In the Argo CD console:

```text
New App
```

Configure:

```text
Application Name: paytm-project
Project: default
Sync Policy: Automatic
Revision: HEAD
Path: k8s
Namespace: paytm-app
```

Set the source repository to your own GitHub repository containing the Kubernetes manifests.

Do not use the reference repository URL in your own project. Use your own repository.

Create the application.

---

## Step 27 — Verify GitOps Deployment

Open the Argo CD application and verify:

- Application status
- Deployment status
- Pods
- Services
- Kubernetes manifests

You can also verify from the terminal:

```bash
kubectl get pods -n paytm-app
kubectl get svc -n paytm-app
kubectl get ingress -n paytm-app
```

When Kubernetes manifests are changed in the Git repository, Argo CD can detect/synchronize the desired state according to the configured sync policy.

---

# Step 28 — Final Project Verification

Run the following checks before considering the project complete.

### AWS / EKS

```bash
kubectl get nodes
kubectl get pods -A
```

### Application

```bash
kubectl get all -n paytm-app
```

### Ingress

```bash
kubectl get ingress -n paytm-app
kubectl get svc -n ingress-nginx
```

### Monitoring

Open:

```text
Prometheus: http://<monitoring-server-ip>:9090
Grafana:    http://<monitoring-server-ip>:3000
```

### Argo CD

Open the Argo CD Load Balancer hostname and verify that the application is synchronized.

---

# Final Project Flow

```text
Developer
   ↓
GitHub
   ↓
Jenkins
   ↓
Build / Test / SonarQube
   ↓
Docker Image
   ↓
Docker Hub
   ↓
Kubernetes / EKS
   ↓
Microservices + Services
   ↓
Ingress
   ↓
Application

Monitoring:
Kubernetes / Jenkins
        ↓
 Prometheus
        ↓
    Grafana

GitOps:
GitHub
   ↓
Argo CD
   ↓
Kubernetes / EKS
```

## Project Completion Checklist

- [ ] AWS VM created
- [ ] IAM configured
- [ ] EKS cluster created
- [ ] kubectl configured
- [ ] Jenkins installed
- [ ] Docker installed
- [ ] Maven installed
- [ ] SonarQube configured
- [ ] Jenkins credentials configured securely
- [ ] Microservice Docker images built
- [ ] Images pushed to Docker Hub
- [ ] Kubernetes namespace created
- [ ] Microservices deployed
- [ ] Kubernetes Services created
- [ ] Frontend deployed
- [ ] NGINX Ingress Controller installed
- [ ] Ingress configured
- [ ] Application verified
- [ ] Prometheus installed
- [ ] Node Exporter installed
- [ ] Jenkins metrics configured
- [ ] Grafana installed
- [ ] Grafana dashboards configured
- [ ] Helm installed
- [ ] Argo CD installed
- [ ] Argo CD application configured
- [ ] GitOps deployment verified
