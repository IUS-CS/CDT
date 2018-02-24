# Architecture

The Android Application will have the following Activities
* Main
* Join
* Host
* Settings
### Startup
When the app is launched, the user is greeted by an activity that prompts them to enter a nickname which is stored in a text file on the user’s Android device. This activity will also allow the user to check what they currently have saved as their nickname.  As long as the nickname is not blank, they will be able to proceed to the rest of the app (and be allowed to later navigate to this activity to change their nickname).
### Main
Once the nickname check passes, they can proceed to the Main activity (which can navigate to the Settings activity which includes the nickname activity and other settings) where they can connect to a server to create a lobby or join another lobby. 
### Join
The user will be able to see who is in the lobby with them, along with the option to leave the lobby.   Each user will be permitted to add a song to the queue, limited by a cool-down timer, and/or number of songs currently in the queue. Song requests will be made to the server using http requests


![](JoinParty.png?raw=true)

### Host
A queue of YouTube videos (or sound only) will play on this activity, powered by the YouTube API. The host will have the ability to remove songs that are deemed inappropriate for the party.


![](HostParty.png?raw=true)

### Settings
Settings allows the user to make changes to app features like the user's nickname. User settings are stored in a file and are loaded when the application is loaded. 

