#About
This application is packaged as an EAR. The `rest-ejb` project contains all the EJBs used by the application including the DAOs and is packaged as a JAR file which lives in the root of the EAR file. The `rest-persistence` project contains the persistence unit and ORM information as well as the models/POJOs used by the application and is packaged as a JAR file which lives in the `lib` folder inside the EAR file. The `rest-web` represents the front-end and is packaged as a web application (WAR file) which lives in the root of the EAR file. This is where the REST (JAX-RS) webservices live and where the EJBs are injected. The front-end was made using AngularJS and consumes directly these RESTful webservices.

com.jonwelzel:rest-ear:ear:1.0-SNAPSHOT
+- com.jonwelzel:rest-web:war:1.0-SNAPSHOT:compile
+- com.jonwelzel:rest-ejb:ejb:1.0-SNAPSHOT:compile
|  +- com.jonwelzel:rest-commons:jar:1.0-SNAPSHOT:compile
|  +- org.apache.commons:commons-lang3:jar:3.0.1:compile
|  +- com.jonwelzel:rest-persistence:jar:1.0-SNAPSHOT:compile


#Installation
1. clone it, or download the zip containing the source
2. run `$ mvn clean install`
3. create a PostgreSQL connection pool + resource in Glassfish (preferrably), deploy `ear`
4. point your browser to `http://localhost:8080/rest/resources/user` for example (empty unless new data is pushed through `curl` POST + JSON or through the [rest-client](https://github.com/jnwelzel/angular-rest-client) app)
5. enjoy, and as always, contributions are more than welcome!