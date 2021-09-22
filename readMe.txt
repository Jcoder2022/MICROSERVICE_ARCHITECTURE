Let us say we have hundreds and thousands of micro services. 
To manage all those services, to get port information, url information, and we 
may scale them up in future also, for all this to maintain, we use service registry.

service registry will maintain all microservices, all microservices will connect to service registry
Let us create Service registry, we will add Eureka Server as dependency.


eureka:
  client:
    register-with-eureka: false
    fetch-registry: false  # Both are false, reason its not going to register to server as it is eureka server
    # in microservices like user-service and department-service we need to provide both above properties and
      # there they will be true
	  
Next, we will add eureka client in pom.xml of both user-service and department-service.	  

# Eureka client configuration
eureka:
  client:
    register-with-eureka: true
    fetch-registry: true
    service-url:
      defaultZone: http://localhost:8761/eureka/
  instance:
    hostname: localhost # we are running locally, if you are running on production, you need to provide production hostname

Next we will give application name in application.yml

spring:
  application:
	DEPARTMENT-SERVICE  

# for user-service
spring:
  application:
	USER-SERVICE  


start executing eureka server 
on browser type localhost:8761
It will show you Instances currently registered with Eureka 
At this point it will show nothing
Next, start executing DEPARTMENT-SERVICE and USER-SERVICE
we can see that DEPARTMENT-SERVICE and USER-SERVICE are registered to service registry
it will show instances in browser
remember, In user service we are calling department-service with localhost.
let us say we have multiple instances of services running,  than it would be difficult to
get the correct url information. So wwe will change the host information like url and we will give the application name
copy DEPARTMENT-SERVICE from browser instance,  and replace it in localhost:9001 where we are calling it from userservice

we need to tell the rest template as well, we are connected to service registry, you need to load balance your request


Next, Let us create GATEWAY API, All requests will traversed to API Gateway, from there all requests will 
go to particular microservice.

Add following dependencies

Eureka client
Gateway
Spring boot actuator 

Add following autowire on main Spring boot Application class
@EnableEurekaClient

Following changes in application.yml file.

server:
  port: 9191 # our department service is at 9001 userservice is at 9002, from now onwards, All our requests will traverse through 9191


# Next we have to do routing configuration,
# All urls having pattern /users should be redirected to user-service microservice
# Similarly, All urls having pattern /departments will be redirected to department-service


spring:
  application:
    name: API-GATEWAY
  cloud:
    gateway:
      routes:  # contains list routes
        - id: USER-SERVICE
          uri: lb://USER-SERVICE # lb is load balance // Application name
          predicates:  # path  information
            - Path=/users/**
        - id: DEPARTMENT-SERVICE
          uri: lb://DEPARTMENT-SERVICE
          predicates:
            - Path=/departments/**


# Eureka client configuration
eureka:
  client:
    register-with-eureka: true
    fetch-registry: true
    service-url:
      defaultZone: http://localhost:8761/eureka/
  instance:
    hostname: localhost # we are running locally, if you are running on production, you need to provide production hostname

#After implementing, we need to start all our services, Now in postman we will enter localhost:9191/users/ with method post 
we will enter user and for department  we will enter localhost:9191/departments/ with method post



{
	"departmentName": "IT",
	"departmentAddress": "London",
	"departmentCode": "IT"
}

{    
	"firstName":" Black " ,
	"lastName" : " Smith ",
	"address":" coventry ",
	"phone" :" 111111111",
	"email":" black@gmail.com",
	"departmentId":"1"
}

// ---------------------------------------------------------------------------------------------//

Next, we will implement circuit breaker. it will identify which of the services are not running and it will run the 
fallback methods available and it will notify user that this service is not running. We will implement hystrix libraries.
and we will implement hystrix dashboard, so that we can identify which of the services are running and which aren't. 
we will do all this in API gateway. 

After adding hystrix dependency, we will goto CloudGatewayApplication class, and autowire enableHystrix
Next, we will do hystrix configuration.
Next we will add FallBackMethodController that will execute when any service is down 
Next, we will do hystrix configuration
 
@RestController
public class FallBackMethodController {

    @GetMapping("/userServiceFallBack")
    public String userServiceFallBackMethod(){
        return "User Service is taking longer than usual. Please try later";
    }

    @GetMapping("/departmentServiceFallBack")
    public String departmentServiceFallBackMethod(){
        return "Department Service is taking longer than usual. Please try later";
    }
}


Next, we need to add configuration where its taking longer time, we will call above particular fallback method.
we will go to application.yml file and we will add filters after predicates.

spring:
  application:
    name: API-GATEWAY
  cloud:
    gateway:
      routes:  # contains list routes
        - id: USER-SERVICE
          uri: lb://USER-SERVICE # lb is load balance // Application name
          predicates:  # path  information
            - Path=/users/**
		  filters:
			- name: CircuitBreaker
			  args:
				name: USER-SERVICE
				fallback: forward:/userServiceFallBack		
        - id: DEPARTMENT-SERVICE
          uri: lb://DEPARTMENT-SERVICE
          predicates:
            - Path=/departments/**
		  filters:
			- name: CircuitBreaker
			  args:
				name: DEPARTMENT-SERVICE
				fallback: forward:/departmentServiceFallBack		


# create hystrix dashboard and enter this in browser http://localhost:9295/hystrix 	

to check hystrix is working we need to enter localhost:9191/actuator/hystrix.stream

it will return following and this shows stream has started
data:{"type":"ping"}

data:{"type":"ping"}

data:{"type":"ping"}

data:{"type":"ping"}

data:{"type":"ping"}

we need to give localhost:9191/actuator/hystrix.stream to our hystrix dashboard next we will press monitor button