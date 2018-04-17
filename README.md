# iSearch

iSearch is a multi-threaded concurrent tool that can scan a number of web pages for a search term.

### Requirements:
You will need JRE 1.8 (but not JDK).

### Build (if JDK installed)
To build the project use:
```
./gradlew clean build
```

### Run
Once you are in the project root directory, you can execute the following command
```
java -jar ./isearch.jar urls.txt regex
```
where `regex` is a case-insensitive regular expression. For example,
```
java -jar ./isearch.jar urls.txt test
```
searches for the word `test` in the contents of all web pages provided in `urls.txt` while
```
java -jar ./isearch.jar urls.txt \\d{4}
```
will find those URLs that contain a word consisting of exactly 4 digits.
To run more complex `regex`es, use `""` around your expression:
```
java -jar ./isearch.jar src/test/resources/test_urls.txt "g(o)+gle"
```

### Input
The program expects a file `urls.txt` in CSV format. The file needs to have a column "URL"
under which there is a list of URLs. The rest of the contents of the file are ignored.

### Output

During execution you will see output that looks like this:

```
21:20 $ java -jar ./isearch.jar urls_50.txt "(http|https)"
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
The output shows each URL being processed and how long it took in milliseconds to process it. The number
at the end of the output shows the total time in milliseconds that the program took to run.

Additionally, a file `results.txt` is created that contains only those URLs that matched the regular expression.

```
21:28 $ cat results.txt 
wordpress.com/
digg.com/
icio.us/
```
