# CICD-Prototype
A prototype CICD pipeline for standardized P3 CICD

## Outline
 - EC2 for Jenkins build server and to host APIs. Server probably needs to be medium or bigger, anything smaller has a tendency to freeze when running a build.
 - S3 Bucket for static site hosting. Every active SPA project will need its own hosting bucket.
 - S3 Bucket for holding secret files (containing credentials, etc.) One bucket can be shared for all projects if we manage it properly.
 - RDS for persistence. One PostGreSQL database server can be shared with all active projects. May need to worry about connection pooling once we have many projects going. Individual projects can split into multiple schemas.

## Policies
 - IAM Role Policy. Applied to EC2 server. Necessary to allow copying to/from S3 buckets using AWS S3 CP command. Not the most secure thing, should get vetted by an expert. May not need all of the actions, and should apply more specifically. Not sure if this renders the bucket policies obsolete?
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

S3 Bucket Policy - Secret bucket, allows copying files from this bucket.
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

S3 Bucket Policy - Hosting bucket, set up for static site hosting.
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
  
Jenkins user account needs to be sudoer so that it can run docker commands? If docker addon is installed correctly this may not be necessary.
```
sudo nano /etc/sudoers  
append "jenkins ALL=(ALL) NOPASSWD: ALL" to the end of the text-file  
```
### [Install mvn](https://awswithatiq.com/how-to-install-apache-maven-on-amazon-linux-2/)
```
sudo yum update -y  
sudo wget https://repos.fedorapeople.org/repos/dchen/apache-maven/epel-apache-maven.repo -O /etc/yum.repos.d/epel-apache-maven.repo  
sudo sed -i s/\$releasever/6/g /etc/yum.repos.d/epel-apache-maven.repo  
sudo yum install -y apache-maven  
```

### [Install node, npm, and angular](https://www.geeksforgeeks.org/how-to-install-angularjs-on-linux/)
```
sudo apt-get upgrade && sudo apt-get update -y  
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.3/install.sh | bash  
. ~/.nvm/nvm.sh  
nvm install 16  
node -e "console.log('Running Node.js ' + process.version)"  
sudo apt install nodejs  
sudo ln -s /usr/bin/nodejs /usr/bin/node  
sudo apt install npm -y  
sudo npm install -g @angular/cli  
```

### Install Docker
```
sudo yum update  
sudo yum install docker  
sudo systemctl start docker  
```

