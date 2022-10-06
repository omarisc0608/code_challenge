# Tribal Credit Line Validator Project

## Prerequisites
-Spring Boot 2.7.4
-Java 11
-Maven 3.8.6
-Lombok
-Groovy
-Spock

## Unit Tests and Integration Tests
-Spock

## Description
API to Determine the credit line of our customers is a key aspect on our daily basis, it to process of determining the credit
line that better suits our customers.

## Structure
Strategy Design Pattern

*CreditLineController
*CreditLineFactory
*SMECalculateCreditLine
*StartupCalculateCreditLine

## Make/Accept a request Credit Line:
Payload example:
{
"foundingType": "SME",
"cashBalance": 1335.30,
"monthlyRevenue": 135.45,
"requestedCreditLine": 100
}

## REQUEST POST
http://localhost:8080/api/tribal/validateCreditLine/

BODY STARTUP
{
"foundingType": "STARTUP",
"cashBalance": 1335.30,
"monthlyRevenue": 135.45,
"requestedCreditLine": 100
}

BODY SME
{
"foundingType": "SME",
"cashBalance": 1335.30,
"monthlyRevenue": 135.45,
"requestedCreditLine": 100
}

## Starting 🚀

### Building 🔧

mvn clean install

## Run 📦

mvn spring-boot:run
