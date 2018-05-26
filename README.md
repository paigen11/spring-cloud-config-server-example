# Config Server Client Setup

### Creating Configuration Properties for your application
1. To create configuration properties for your application, visit the following Github location.
[Configuration Properties](TBD)

2. Create a new file with the following naming convention.
```
[application name]-[life cycle profile].yml
ex. my-app-to-config-QA.yml
```

3. Add your application specific configuration properties into this file in Yaml format.  You can use the following [link](https://learnxinyminutes.com/docs/yaml/) to learn more about the Yaml format.

4. Save the file and proceed with setting up your Config Client.


### Attaching to the Cloud Config Server with Spring Boot and Gradle
1. Ensure that you have the following dependencies in Gradle.

```
compile group: 'org.springframework.cloud', name: 'spring-cloud-starter-config', version:'1.4.3.RELEASE'
compile group: 'org.springframework.boot', name: 'spring-boot-configuration-processor', version: "${springBootVersion}"
```

2. Create a bootstrap.yml file in "src/main/resources" and put in the following contents:
```yml
spring:
  application:
    name: ##APPLICATION NAME GOES HERE##
  cloud:
    config:
      uri: https://myexample-config-server.non-prod.com
---
spring:
  profiles: production
  cloud:
    config:
      uri:  https://myexample-config-server.prod.com
```

3. In the bootstrap.yml file, change the ##APPLICATION NAME GOES HERE## to your application name that you used in Step#2 of `Creating Configuration Propertiesfor your application`. ex. my-app-to-config

4. Ensure that you set SPRING_PROFILES_ACTIVE in your PCF manifests or if you're running locally.  The current values would be "QA" for the QA environment or "Q1" for the Q1 enviroment.  

### Binding to your Configuration Properties with Spring
1. Create a class called "ConfigProperties" and add the following annotations to it:
```java
@Configuration
@ConfigurationProperties
public class ConfigProperties {
    
}
```

2. Let's say that you have the completed all of the steps from `Creating Configuration Properties for your application` and `Attaching to the Cloud Config Server with Spring Boot and Gradle`.  And the yml file that you created had the following contents:
```
configurations:
  featureToggleFlag: true
  my-custom-flag: false
```

3. If you notice, the properties are prefixed by `configurations`.  To bind these properties to the ConfigProperties class, you'll first need to add a "prefix" to the @ConfigurationProperties annotation. ex.
```java
Configuration
@ConfigurationProperties(prefix = "configurations")
public class ConfigProperties {
    
}
```

4. To bind the "featureToggleFlag" property, add the following member variable. along with the getter and setter.
```java
Configuration
@ConfigurationProperties(prefix = "configurations")
public class ConfigProperties {

   private boolean featureToggleFlag;
   
   //Add Getter and Setter here...
    
}
```
If you match the member variable name with the actual property name in the configuration properties, Spring will automatically bind it to the member variable.

5. However, let's say that you want to also bind the second property "my-custom-flag" in the ConfigProperties class.  Java won't allow you to create a member variable with dashes.  The workaroud would be to create a member variable with the camel cased equivalent. ie. "myCustomFlag". Then use Spring's @Value annotation to bind it to the configuration property. 
```java
Configuration
@ConfigurationProperties(prefix = "configurations")
public class ConfigProperties {

   private boolean featureToggleFlag;
   
   @Value("${my-custom-flag}"
   private boolean myCustomFlag;
   //Add Getter and Setter here...
    
}
```

6.  Once you've completed the ConfigProperties class, the last step is to autowire the ConfigProperties class into what ever class that will need to properties.  You'll use the getter methods to access the values.
