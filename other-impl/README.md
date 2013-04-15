
# The non-camel implementation

### Quickstart

The quickest (and best) way to get started to is do the following: 

1. Clone the repository locally
2. Build the repository as follows:     
    mvn3 clean install -DskipTests -Dfull
3. Open the [JmsIntegrationTest]() or the [RestIntegrationTest](). 
4. Look at the code in the `@Test` methods. 
5. Run the tests. 
6. Modify the test methods to your heart's content. 
7. Repeat steps 5. and 6. until you get tired. 

### Content

This module contains the following: 

- A stateless EJB that processes all requests ("ProcessRequestBean")
  - FYI, I will throw a tantrum if we start using Threads instead of EJB's in app-server
    implementatoins. For very good reasons, I might add. 
- A JMS bean that, through configuration in an ejb-jar.xml, is configured to listen to multiple
  (hornetq wildcarded) queues. More about JMS queues below. 
- A RestEasy REST service implementation that is fairly compact for a number of reasons, including
  - Regular expression use in the `@Path` annotations. 
  - Modularization of messaging, serialization and processing of the requests. 
    - The code that relates to message structure and serialization can be found in the client
      module. 
- Other support classes necessary for CDI injection, etc. 

### The JMS implementation

There are couple of notes for the JMS implementation. These notes explain more about the eventual
design than about the code: 
- Queue structure
- MessageDriven bean design

One last note, I unfortunately spent some time creating code to dynamically create JMS queues (based
on a user request) in AS 7/EAP 6. The code for that can be found in the [DynamicQueueCreationBean](
) class.

### The REST implementation



