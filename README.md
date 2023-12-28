# CICD-Prototype
A prototype CICD pipeline for standardized P3 CICD

## Outline
 - EC2 for Jenkins build server and to host APIs. Server probably needs to be medium or bigger, anything smaller has a tendency to freeze when running a build.
 - S3 Bucket for static site hosting. Every active SPA project will need its own hosting bucket.
 - S3 Bucket for holding secret files (containing credentials, etc.) One bucket can be shared for all projects if we manage it properly.
 - RDS for persistence. One PostGreSQL database server can be shared with all active projects. May need to worry about connection pooling once we have many projects going. Individual projects can split into multiple schemas.

## Policies
### IAM Role Policy
Applied to EC2 server. Necessary to allow copying to/from S3 buckets using AWS S3 CP command. Not the most secure thing, should get vetted by an expert. May not need all of the actions, and should apply more specifically. Not sure if this renders the bucket policies obsolete?
 ```JSON
 {
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "VisualEditor0",
            "Effect": "Allow",
            "Action": [
                "s3:ListStorageLensConfigurations",
                "s3:ListAccessPointsForObjectLambda",
                "s3:GetAccessPoint",
                "s3:PutAccountPublicAccessBlock",
                "s3:GetAccountPublicAccessBlock",
                "s3:ListAllMyBuckets",
                "s3:ListAccessPoints",
                "s3:PutAccessPointPublicAccessBlock",
                "s3:ListJobs",
                "s3:PutStorageLensConfiguration",
                "s3:ListMultiRegionAccessPoints",
                "s3:CreateJob"
            ],
            "Resource": "*"
        },
        {
            "Sid": "VisualEditor1",
            "Effect": "Allow",
            "Action": "s3:*",
            "Resource": "arn:aws:s3:::*"
        }
    ]
}
 ```
 ### IAM Role Trust Relationship
 ```JSON
 {
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Principal": {
                "Service": "ec2.amazonaws.com"
            },
            "Action": "sts:AssumeRole"
        }
    ]
}
 ```

### S3 Bucket Policy - Secret Bucket
Allows copying files from this bucket.
```JSON
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "SaveObjects",
            "Effect": "Allow",
            "Principal": "*",
            "Action": "s3:GetObject",
            "Resource": "arn:aws:s3:::jenkins-secret-bucket/*"
        },
        {
            "Sid": "ListObjects",
            "Effect": "Allow",
            "Principal": "*",
            "Action": "s3:ListBucket",
            "Resource": "arn:aws:s3:::jenkins-secret-bucket"
        }
    ]
}
```

### S3 Bucket Policy - Hosting Bucket
Set up for static site hosting.
```JSON
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "SaveObjects",
            "Effect": "Allow",
            "Principal": "*",
            "Action": [
                "s3:GetObject",
                "s3:PutObject"
            ],
            "Resource": "arn:aws:s3:::p3-static-hosting/*"
        },
        {
            "Sid": "ListObjects",
            "Effect": "Allow",
            "Principal": "*",
            "Action": "s3:ListBucket",
            "Resource": "arn:aws:s3:::p3-static-hosting"
        }
    ]
}
```


## Server
### Install JDK
```
sudo yum install java-17-amazon-corretto-devel
```


### [Install Jenkins](https://www.jenkins.io/doc/tutorials/tutorial-for-installing-jenkins-on-AWS/)
```
sudo yum update -y
sudo wget -O /etc/yum.repos.d/jenkins.repo https://pkg.jenkins.io/redhat-stable/jenkins.repo
sudo rpm --import https://pkg.jenkins.io/redhat-stable/jenkins.io.key
sudo yum upgrade
sudo amazon-linux-extras install java-openjdk11 -y
sudo yum install jenkins -y
sudo systemctl enable jenkins
sudo systemctl start jenkins
sudo systemctl status jenkins
```
Newer jenkins public key import?
```
sudo wget -O /etc/yum.repos.d/jenkins.repo https://pkg.jenkins.io/redhat-stable/jenkins.repo
sudo rpm --import https://pkg.jenkins.io/redhat-stable/jenkins.io-2023.key
yum install fontconfig java-17-openjdk
yum install jenkins
```

Set JAVA_HOME in jenkins config
```
/usr/lib/jvm/java-17-amazon-corretto.x86_64/bin
```

Making Jenkins acct sudoer shouldn't be necessary if docker is confiogured properly.
```
sudo nano /etc/sudoers  
append "jenkins ALL=(ALL) NOPASSWD: ALL" to the end of the text-file  
```

### Install Git
```
sudo yum install git
```
Set Git installation in jenkins config
```
/usr/bin/git
```

### [Install Maven](https://docs.aws.amazon.com/neptune/latest/userguide/iam-auth-connect-prerq.html)
[old - same procedure](https://awswithatiq.com/how-to-install-apache-maven-on-amazon-linux-2/)
```
sudo yum update -y  
sudo wget https://repos.fedorapeople.org/repos/dchen/apache-maven/epel-apache-maven.repo -O /etc/yum.repos.d/epel-apache-maven.repo  
sudo sed -i s/\$releasever/6/g /etc/yum.repos.d/epel-apache-maven.repo  
sudo yum install -y apache-maven  
```

### [Install Docker](https://www.cyberciti.biz/faq/how-to-install-docker-on-amazon-linux-2/)
With the groups set up properly jenkins doesn't need to be a sudoer to do docker commands.
```
sudo yum install docker
sudo usermod -a -G docker jenkins
newgrp docker
sudo systemctl enable docker.service
sudo systemctl start docker.service
```


### [Install NodeJS & npm](https://docs.aws.amazon.com/sdk-for-javascript/v2/developer-guide/setting-up-node-on-ec2-instance.html)
```
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.3/install.sh | bash
. ~/.nvm/nvm.sh
nvm install 16
```
Version 16 is specified as LTS version 18 is not supported on amazon linux 2 image.

### Install Angular
```
sudo npm install -g @angular/cli
```
