# CDT
CDT brings music collaboration to parties and other social gatherings through an easy to use Android application. It utilizes youtube music videos to deliver a wide variety of free music content to users while taking the headaches out of a single person creating playlists for a diverse group of people. The app consists of two modes, hosting and contributing. The hosting mode contains controls for moderating music while the contributing mode allows users to suggest, upvote, and downvote songs.
 
## Getting Started

### Prerequisites
* [Go 1.6](https://www.digitalocean.com/community/tutorials/how-to-install-go-1-6-on-ubuntu-16-04)
* [Android Studio 3.0](https://developer.android.com/studio/index.html)

### Compiling and Running
To run the server:

	go run src/server/server.go

To compile server to a binary then run 

	go build src/server/server.go
	src/server/server

For information on compiling and running the CDT android application in visual studio go [here](https://developer.android.com/studio/run/index.html)
	
## Running the tests
Server tests should be ran with the built in go test command

	cd src/server/
	go test 

The CDT Android Application utilizes the junit test suite in Android Studio. For information on running junit tests with android studio go [here](https://developer.android.com/training/testing/unit-testing/local-unit-tests.html)

## License
All rights to this project are reserved- see LICENSE file for details
