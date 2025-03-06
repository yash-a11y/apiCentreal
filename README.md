# Deploying Spring Boot Application on AWS EC2 (VPC Public Subnet)

## Prerequisites
Ensure you have the following:
- AWS account with IAM user access
- AWS EC2 instance (Amazon Linux 2 or Ubuntu recommended)
- Security Group allowing inbound traffic (HTTP, HTTPS, and SSH)
- AWS VPC with a public subnet
- Spring Boot application (packaged as a JAR)
- SSH client (e.g., PuTTY, Terminal)
- AWS CLI installed and configured
- Java and Maven installed on the EC2 instance

## Step 1: Launch an EC2 Instance
1. Log in to AWS Console.
2. Navigate to **EC2 > Instances** and click **Launch Instance**.
3. Choose an AMI (Amazon Linux 2, Ubuntu, etc.).
4. Select an instance type (e.g., t2.micro for free tier).
5. Configure instance details:
   - Choose your VPC and select a public subnet.
   - Enable **Auto-assign Public IP**.
6. Configure storage and tags as needed.
7. Configure security group:
   - Allow **port 22** for SSH.
   - Allow **port 8080** (or your Spring Boot app port) for public access.
8. Launch the instance and download the key pair (.pem file).

## Step 2: Connect to the EC2 Instance
1. Open Terminal or PuTTY.
2. Connect using SSH:
   ```sh
   ssh -i your-key.pem ec2-user@your-ec2-public-ip
   ```
Note : Dont use private/office network cause rise error of timeout, timeout also occur is you does not setup inboud rule for ssh properly in security group, another to get error in connection is permission of your-key.pem file.
## Step 3: Install Java and Maven on EC2 Instance
For Amazon Linux:
```sh
sudo yum update -y
sudo yum install -y java-23 (support this project)
```

## Step 4: Transfer Spring Boot JAR to EC2
On your local machine, use SCP to upload your JAR file:
```sh
scp -i your-key.pem your-app.jar ec2-user@your-ec2-public-ip:/home/ec2-user/
```
Otherwise use filezilla client : https://filezilla-project.org/download.php
connect  with EC2 remote instance with its public id and .pem file
## Step 5: Run Spring Boot Application on EC2 Instance
1. Navigate to the directory:
   ```sh
   cd /home/ec2-user/
   ```
2. Start the application:
   ```sh
   java -jar your-app.jar 
   ```
- Make sure your **security group** allows inbound traffic on port 8080.
- Access your app using `http://your-ec2-public-ip:8080` in a browser.

  
