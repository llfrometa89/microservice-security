### Steps to run 

1. Create `.env` with the below structure:  
  
```  
export AWS_SECRET_KEY=<AWS_SECRET_KEY>  
export AWS_ACCESS_KEY=<AWS_ACCESS_KEY>  
export AWS_REGION=<AWS_REGION>
export AWS_COGNITO_USER_POOL_ID=<AWS_COGNITO_USER_POOL_ID>

```
  
2. Run at terminal:  
  
```  
$ source .env && sbt run  
```