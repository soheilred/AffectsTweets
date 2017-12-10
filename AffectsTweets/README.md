
# AffectsTweets
#### Project for CS825 Team 3 ####

### Requirements
* Java 1.8+
* Maven

### Installation
You have two options:

####Build with maven
Change the current directory to `AffectsTweets` then:
```
mvn package
```
two `jar` files will be created in `target` directory.

#### Run the jar file
 Run the jar files with the following command:
```
java -jar target/AffectsProj-3.1.4-jar-with-dependencies.jar
```
\_________________ 


\_________________ 
##### SVM:
Change the current directory to SVM folder.
Modify file RankLib; this file contains the features scores obtained from different tf-idfs and run the command:
$ python regressionAT.py

The output file will be written on svm.results

##### Twitter API:
Install the package Twitter API using either `pip` or the provided package in Twitter API folder.
modify the file twitter-search.py in `line 29` the variable q:

`query = twitter.search.tweets(q="hahaha", lang='en', count=1000)`

to get the latest tweets for your desire keyword. Results will be saved in a file named: output.tweets


