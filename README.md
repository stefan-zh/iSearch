# iSearch

iSearch is a tool that can scan a number of web pages for a search term.

#### Requirements:
You will need JRE 1.8 (but not JDK).

#### Build
To build the project use:
```
./gradlew clean compileJava jar
```

#### Run
Once you are in the project root directory, you can execute the following command
```
java -jar build/libs/isearch.jar urls.txt regex
```
where `regex` is a regular expression. For example,
```
java -jar build/libs/isearch.jar urls.txt test
```
searches for the word `test` in the contents of all web pages provided in `urls.txt` while
```
java -jar build/libs/isearch.jar urls.txt \\d{4}
```
will find those URLs that contain a word consisting of exactly 4 digits.

#### Input
The program expects a file `urls.txt` which is in CSV format. It needs to have a column titled "URL"
under which there is a list of URLs. The rest of the contents of the file are ignored.

#### Output

During execution you will see output similar to this:

```
21:20 $ java -jar build/libs/isearch.jar urls_50.txt "(http|https)"
google.com/: 1715ms
twitter.com/: 1793ms
facebook.com/: 1922ms
wordpress.org/: 324ms
adobe.com/: 381ms
youtube.com/: 2427ms
wikipedia.org/: 407ms
linkedin.com/: 380ms
...
8830
```
The output shows each URL being processed and how long in milliseconds it took to process it. At the end 
the number shows the total time in milliseconds that took the program to run.

Additionally, there will be a file called `results.txt` that will contain only those URLs that matched the
regular expression.

```
21:28 $ cat results.txt 
wordpress.com/
digg.com/
icio.us/
```
