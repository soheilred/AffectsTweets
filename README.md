# Affects Tweets
#s

### Requirements
* Java 1.8+
* Maven

### Installation
You have two options:

##### 1. Run the prebuilt jar files (for results test purpose)
Edit the following and run it in CLI according to the assignment number and version:
```
java -jar outputs/PAx-vx.x.x.jar 
```
 
##### 2. Build by maven then run the jar files on your own
There is an `<mainClass>edu.unh.cs.ir.paX.AssignmentX</mainClass>` tag in `pom.xml`which `X` is the number of the assignment located at `src`.
 Change that line into the desired assignment number then build the project by maven:
``` 
mvn package 
```
Two `jar` files will be created in `target` directory. Run the jar files using the following command:
``` 
java -jar target/InfoRet-x.x.x-jar-with-dependencies.jar 
```
