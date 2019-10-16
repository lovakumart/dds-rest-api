# Drone Delivery API

Drone Delivery API is an application to manage the online orders and deliveries to the customers effectively. This application is capable enough to determine the best way to deliver the package which helps the customer and Walmart. This will also help to determine the NPS value which is determined by customer satisfication.

Assumptions
============
- Orders will be grouped and available in flat files (.txt) scheduled for each day/drone.
- Each Order will be updated realtime depending on previous order delivery and current delivery start time.
- The Order to be delievered will be determined based on the sorted list of orders assigned, considering the distance from Warehouse to Customer Location.
- NPS (Net Promoter Score) is calculated for orders completed/delivered.
- One Drone can carry/deliver to one customer at a time.
- The time taken for Drone from Warehouse to Delivery Location is same as time taken to return to Warehouse.
- The total ETA per delivery will be considering the round-trip, which will be the start time for next Order Delivery (Time for loading the next order to Drone not considered).
- The delivery schedule is calculated considering All Good!! conditions i.e.; No Bad Weather, No Drone Issues etc.
- If the estimated delivery time is beyond Drone Daily Closing Time i.e.; 10:00:00pm, they will be scheduled for later.
- If there is an input file then there are orders, if no orders there will not be any file.

>Enhance/TODO
- Make this available as Rest Service.
- Add Email Utility to send notifications on the Order Status to Customer and Walmart.
- Batch process to determine the Undelivered orders and add to the next day list.

>Unknowns:
- What is the time taken for loading the Drone with next Order? We need to add this to the Delivery Start Time.
- Will the orders which are not delivered should be handled first before the next order file on next day?
- What to do in case of multiple orders from the same customer around same time?
- What is the capacity or weight that Drone can handle? Should we consider this based on the package weight?

Technical Details 
=================
- Langauge: Java 
- Test: JUnit 
- Build/Package: Apache Maven 
- Code Coverage: Jococo 

Design Details
==============
This is developed as an simple java application, to handle or manage the orders as needed. Considered using traditional design patterns like Singleton while development.

>Packages
- config => This will define all the final static variable which can be used by the application
- controller => This include class(es) to handle the request when hosted as API
- endpoint => This has the class(es) which has the basic operations endpoints for each request
- exception => This has the custom Exception handling logic for different error scenarios
- factory => This has the factory classes which will help to perform any data transform/manipulation
- model => This has the model of the Order details, with all required attributes
- service => This holds the operations to be performed i.e.; handling the request and apply business logic
- util => This holds the utility classes which will help to determine the generic operations i.e.; calculate NPS, calculate distance etc.

Running the Application
=======================
>Pre-requisites
- Java, Maven is installed and validated.

1. Clone the repository provided
2. Using any CLI (CMD or GIT BASH) go to application root directory and run below command
         "**mvn clean package**"
3. Go into target directory and check for packaged .jar file (e.g.; dds-rest-api-0.0.1.jar)

>Execute:
- Run below command from CLI

"**java -jar (jar file to execute) (absolute path)**"

* Jar file: target/dds-rest-api-0.0.1.jar
* Absolute Path: D:/Walmart-DDS/data/inputData.txt
