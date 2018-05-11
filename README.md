# BooksCollectionsAPI

Building
To build the project use following command:
.................................................
`mvn clean package`

Running
After building the application run following command to start it:
.................................................
`mvn exec:java -Dexec.mainClass="com.example.index.Main" -Dexec.args="books.json"`

The application starts on port 8080 (it can be changed in the main class).
The first and only argument is the path to the file ("books.json").
