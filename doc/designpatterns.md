# Design Patterns

## Design Patterns Currently in Use
*	Iterator	- 	The application will use a list structure to store user submitted songs.  For our purposes, the song collection will be treated as a queue and always play the song at the front of the list to keep the order of the songs played fair (first come first serve).
*	Singleton 	-	The application needs to have a single instance of the class for every unique lobby that is created so that all connected users are able to add songs to the same list.
*	Chain of Responsibility		-	The application uses a GUI with buttons that require the use of events and handler.  The buttons create events/requests and other objects such as text files and video players handle them.


## Possible Design Pattern Additions
*	Builder		-	Considering that the application will need a Host (the first user to connect to a lobby), the Host may need extra abilities than just adding songs to the list.  This may include Starting/Stopping playback or passing the role of Host to another user (such as when the Host wants to leave the lobby.
*	Mediator 	-	It would be impractical to connect all users in a lobby to each other, especially if many users connect to the same lobby.  Instead, the lobby acts as the mediator between users. Users give information to the lobby and the lobby can give the users any information that they request.  For example, instead of every client storing every user who is connected to the lobby, the lobby stores a list of the users, and if a user is interested in who is connected to it, the lobby can give that information to the user that is requesting that information.

## Continued Design
*	Iterator	-	A possible feature to be added is converting the the way songs are stored and played.  Instead of treating it as a queue in which songs are played in the order they were submitted, we may implement a priority queue.  Users would be able to vote on the submitted songs that they like the most, and the songs with the most votes would move to the top of the queue and get played first.