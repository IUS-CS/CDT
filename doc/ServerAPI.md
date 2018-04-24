# The People's Playlist
## API Documentation

Some terms used in this API:
* `party` - A collection of songs
* `name` - The name of the party
* `id` - The id of the user
* `songID` - The youtube song ID

Example of JSON Party Data:

	{
		"name": "c346",
			"creator": "895476254173427895839",
			"songs": [
			{
				"uploader": "895476254173427895839",
				"id": "H4UtBYUMVJk",
				"title": "the xx- intro (seamless edit)",
				"imageUrl": "https://i.ytimg.com/vi/H4UtBYUMVJk/default.jpg",
				"upvotes": 1,
				"downvotes": 0
			},
			{
				"uploader": "895476254173427895839",
				"id": "CwfoyVa980U",
				"title": "Charlie Puth - \"How Long\" [Official Video]",
				"imageUrl": "https://i.ytimg.com/vi/CwfoyVa980U/default.jpg",
				"upvotes": 1,
				"downvotes": 3
			}
			]
	}

|||
| ----------- | ----------- |
| **Title:** | Get Party | 
| **URL:** | /party/{name} |
| **Method:** | GET | 
| **URL Params:** | `Required:` id=[string] | 
| **Response Codes:** | Success (200 OK)] <br/>Not Found (404) | 
|||
| **Title:** | Create Party | 
| **URL:** | /party/{name} |
| **Method:** | GET | 
| **URL Params:** | `Required:` id=[string] | 
| **Response Codes:** | Success (201 Created)] <br/> Forbidden(403 Party already exists) | 
|||
| **Title:** | Delete Party | 
| **URL:** | /party/{name} |
| **Method:** | GET | 
| **URL Params:** | `Required:` id=[string] | 
| **Response Codes:** | Success (200 OK)] <br/> Not Found (404) <br/> Forbidden(403 Not creator of party) | 
|||
| **Title:** | Add Song | 
| **URL:** | /party/{name}/{songID} |
| **Method:** | GET | 
| **URL Params:** | `Required:` id=[string]<br/>title=[string]<br/>imageUrl=[string] | 
| **Response Codes:** | Success (201 Created)] <br/> Not Found (404 Party not found) <br/> Upvoted (200 Song already exists, upvoted) | 
|||
| **Title:** | Delete Song | 
| **URL:** | /party/{name}/{songID} |
| **Method:** | GET | 
| **URL Params:** | `Required:` id=[string] | 
| **Response Codes:** | Success (200 OK)] <br/> Not Found (404) <br/> Forbidden(403 Not creator of party or song) | 
|||
| **Title:** | Upvote Song | 
| **URL:** | /party/{name}/{songID}/downvote |
| **Method:** | POST | 
| **URL Params:** | `Required:` id=[string] | 
| **Response Codes:** | Success (200 OK)] <br/> Not Found (404) | 
|||
| **Title:** | Delete Song Upvote | 
| **URL:** | /party/{name}/{songID}/downvote |
| **Method:** | DELETE | 
| **URL Params:** | `Required:` id=[string] | 
| **Response Codes:** | Success (200 OK)] <br/> Not Found (404) | 
|||
| **Title:** | Downvote Song | 
| **URL:** | /party/{name}/{songID}/downvote |
| **Method:** | POST | 
| **URL Params:** | `Required:` id=[string] | 
| **Response Codes:** | Success (200 OK)] <br/> Not Found (404) | 
|||
| **Title:** | Delete Song Downvote | 
| **URL:** | /party/{name}/{songID}/downvote |
| **Method:** | DELETE | 
| **URL Params:** | `Required:` id=[string] | 
| **Response Codes:** | Success (200 OK)] <br/> Not Found (404) | 
