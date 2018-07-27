#Parser
@Author: Eduardo Mendoza Aguilar
@Date: 2018-07-27

This program permitted to parse and block IP's address if they are into the dates and attempts interval.

First at all, please change the values of lines 20 and 21 to ensure the mysql connection:

		myDataSource.setUser(?); // change this value
		myDataSource.setPassword(?); // change this value

After this, you can execute the db.sql file as follow:

mysql> source db.sql;

This will create the database "parser" and the tables "requests" and "blocked"

Once the data base has been created, you can execute the jar file using the accesslog file param as follow:

java -cp "target/parser.jar" com.ef.Parser --accesslog=/Users/eduardomendoza/Downloads/Java_MySQL_Test/access.log --startDate=2017-01-01.13:00:00 --duration=hourly --threshold=100 

Each time the "accesslog" parameter is included its content will be inserted into "requests", be aware that there are no verifications for repetitions,
which means, more than one execution adding this parameter will duplicate the instances in "requests" table.

The execution of the Parser is as follow:

java -cp "target/parser.jar" com.ef.Parser --startDate=2017-01-01.00:00:00 --duration=daily --threshold=500

Each execution will insert a row into "blocked" table for each IP obtained this way. Due it include a DATETIME of insert
the IP could be blocked more than once, this DATETIME parameter allows to be included a period of blocked that could be
decided using the time it has been in the "blocked" table.

Finally, the queries.sql file include the queries requested.

THIS SYSTEM WAS CREATED WITH MAVEN, TO BUILD THE SYSTEM USING THE SOURCE CODE USE:

mvn clean package
