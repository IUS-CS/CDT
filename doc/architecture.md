# Startup
When the App is launched, the user is greeted by an Activity that prompts them to enter a NickName which is stored in a text file on the user’s Android device. This Activity will also allow the user to check what they currently have saved as their NickName.  As long as the NickName is not blank, they will be able to proceed to the rest of the App (and be allowed to later navigate to this Activity to change their NickName).

# Main Activity
Once the NickName check passes, they can proceed to the Main Activity (which can navigate to the Settings Activity which includes the NickName Activity and other settings) where they can connect to a server to create a lobby or join another lobby. The user will be able to see who is in the lobby with them, along with the option to leave the lobby. A Queue of YouTube videos (or sound only) will play on this Activity, powered by the YouTube API.  Each user will be permitted to add a song to the Queue, limited by a cooldown timer, and/or number of songs currently in the Queue.

# Server
The client will make http requests and the server will update and send JSON data to the clients connected to the lobby