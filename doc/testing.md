## Server Testing
Server tests are created using [postman](https://www.getpostman.com/) and ran using the [newman](https://www.getpostman.com/docs/v6/postman/collection_runs/command_line_integration_with_newman) collection runner. We chose to use postman over other testing frameworks for its ease of setup and its ability to quickly make tests while using it as a development tool. Tests are ran locally before pushing changes to the server.


### Prerequisites
* Go 1.6
* Postman 
* Newman

Example of how to run tests:

First run the server in the background:
	go run Server/Server.go& 

Then run the newman collection runner on the postman collection:
	newman run Server/CDT_API.postman_collection.json 
	
Upon completion, the server process should be killed before runnning another test.

The output should be resemble the following:
![Newman test run](ExampleTest.png)

This testing framework is also used to populate the server with data for manual tests. 
	newman run Server/PopulateServer.json 

