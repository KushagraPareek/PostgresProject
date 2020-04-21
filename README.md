/****************************************************************************

CSE532 -- Project 2 File name: woco1.sql Author: KUSHAGRA PAREEK (112551443) Brief description: Readme

****************************************************************************/

/** I pledge my honor that all parts of this project were done by me alone and without collaboration with anybody else. **/

There are alternative ways to run the project, a brief user guide is included in the project report.

Files hierachry.

The folder contains files as follows.

woco1.sql ---------------------------creates the database and inserts the data into the database.

WOCO.war ----------------------------required to run project on web server.

PostgresConnector.java---------------Java file for connecting to the database

Query.java---------------------------Java servlet to query database and send results to the web server.

views.sql, functions.sql-------------These are sql functions and views, they are are not required to be used during installation
                                     their functionality is included in Query.java

Install using

*****Download and Install postgresql.

*****Download and Install JAVA version 8 or above.

*****Create the woco1 database using the file provided woco1.sql in the zip folder.

*****Use command sudo -u postgres psql postgres -f “/filelocation/woco1.sql”

*****The above step will create the database and Insert data into the database.

*****Now from the zip file copy .war file and place it on

*****/apache-tomcat-/webapps/ ​ folder.

*****To start the tomcat server, go to ​ /apache-tomcat-9.0.0.M10/bin

*****and do ./catalina.sh start

*****You will be able to see the index page on the public DNS.

Alternatively The project can by run by simply importing the war file in the eclipse environment and using an appropriate

server.The eclipse environment should be configured to support dynamic web projects.
