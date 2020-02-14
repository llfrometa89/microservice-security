# Microservice Security
[![CircleCI](https://circleci.com/gh/llfrometa89/microservice-security/tree/master.svg?style=svg&circle-token=12861a4197aea250799aff47ae08e899cc47fd58)](https://circleci.com/gh/llfrometa89/microservice-security/tree/master)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/bf1857fca10240ce9cf4076b67430d8c)](https://www.codacy.com?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=llfrometa89/microservice-security&amp;utm_campaign=Badge_Grade)
[![Codacy Badge](https://api.codacy.com/project/badge/Coverage/bf1857fca10240ce9cf4076b67430d8c)](https://www.codacy.com?utm_source=github.com&utm_medium=referral&utm_content=llfrometa89/microservice-security&utm_campaign=Badge_Coverage)

## Steps to run 

- Create `.env` with the below structure:  
  
```  
export AWS_SECRET_KEY=<AWS_SECRET_KEY>  
export AWS_ACCESS_KEY=<AWS_ACCESS_KEY>  
export AWS_REGION=<AWS_REGION>
export AWS_COGNITO_USER_POOL_ID=<AWS_COGNITO_USER_POOL_ID>

```
  
- Run at terminal:  
  
```  
$ source .env && sbt run  
```