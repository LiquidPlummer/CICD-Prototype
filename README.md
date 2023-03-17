# CICD-Prototype
A prototype CICD pipeline for standardized P3 CICD

## Outline
 - EC2 for Jenkins build server and to host APIs. Server probably needs to be medium or bigger, anything smaller has a tendency to freeze when running a build.
 - S3 Bucket for static site hosting. Every active SPA project will need its own hosting bucket.
 - S3 Bucket for holding secret files (containing credentials, etc.) One bucket can be shared for all projects if we manage it properly.
 - RDS for persistence. One PostGreSQL database server can be shared with all active projects. May need to worry about connection pooling once we have many projects going. Individual projects can split into multiple schemas.

## Policies
 - DevOps IAM role. Applied to EC2 server. Necessary to allow copying to/from S3 buckets using AWS S3 CP command.
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

