# crypto-currency-converter
This application will convert one unit of crypto currency to local currency price.

### Prerequisites
Before getting started, make sure you have the following tools installed:

* Java Development Kit (JDK) 8 or later
* Apache Maven
* Docker

### Build Project in local

Clone this repo in local.

Navigate the project repository. Ex: crypto-currency-converter

Run maven package command. This command creates a war file at /target

```mvn clean package```


### Create and deploy on docker

Run this command to create docker image and deploy in local host.

```docker-compose up --build```

Now access http://localhost:8080/login to access the crypto-converter application.






