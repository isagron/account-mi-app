# Account-mi
Simple application for managing private bank account. Help to monitor incomes/outcomes and receive statistics.

##General
###Modules
This project include three modules
1. account-mi-frontend - user interface base on Angular 9
2. account-mi-server - web service implement base on spring boot
3. account-mi-client - java library including all DTOs and rest clients.

###Technologies
1. Angular 9
2. Spring boot 2.3.4.RELEASE
3. MongoDB
4. Firebase (Authentication server)

###Requirements
1. MongoDB server
2. Firebase account - only if you want to work in secure mode

##Build/run & configuration

###Configuration
```yaml
# Enable/disable security using firebase
security:
  firebase-props:
    enable: false
    # Require only if security.firebase-props.enable is true  
    api-key: "${firebase-api-key}"
    account:
      type: "service_account"
      "project-id": "account-mi"
      "private-key-id": "${firebase-private-key-id}"
      "private-key": "${firebase-private-key}"
      "client-email": "${firebase-client-email}"
      "client-id": "109246710678736864849"
      "auth-uri": "https://accounts.google.com/o/oauth2/auth"
      "token-uri": "https://oauth2.googleapis.com/token"
      "auth-provider-cert-url": "https://www.googleapis.com/oauth2/v1/certs"
      "client-cert-url": "${firebase-client-cert-url}"
  enable: false

# Set cross origin
  allowed-origins:
    - ${frontend.dev.server}
  allowed-methods:
    - GET
  allowed-headers:
    - Authorization
  allowed-public-apis:
    - /favicon.ico
    - /v1/auth/signin
    - /v1/auth/signup
    - /v3/api-docs/**
    - /configuration/**
    - /swagger*/**
    - /webjars/**
    - /swagger-resources/**
    - /sw.js
  exposed-headers:
    - X-Xsrf-Token
# If true, it will inject the user id from principal as query param in every request
web-filters:
  inject-user-id: false
```

###Build / Run
####Intellij
Set the following environment variables through from run configuration or add them to the properties files according to the chosen profile.
All the variables related to firebase can be copy from your application firebase account
```
spring.profiles.active=<dev | unsecure | integration >;
//if you run the frontend application in dev
frontend.dev.server=http://localhost:4200;
//db
db.host=localhost;
db.port=27017;
// require only for integration profile
firebase-api-key=<api-key>>;
firebase-private-key-id=<private-key-id>;
firebase-private-key=<private-key;
firebase-client-email=<client email>;
firebase-client-id=<client-id>;
firebase-client-cert-url=<certificate-url>
```

####Docker
Download the images from docker hub
```
docker pull innon/account-mi-server
docker pull innon/account-mi-client
```
Run the container using the images
```
docker run innon/account-mi-server
docker run innon/account-mi-server
```
Note! you will need to set the environment variable has defined when running from IDE

####Docker compose
From root directory run
note! make sure to change the value of environment variables according to your setup
```
docker-compse up --build
```

#### K8T
From root directory
```
kubectl apply -f k8t
```