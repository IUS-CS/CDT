package main

import (
	"encoding/json"
	//"fmt"
	"github.com/gorilla/mux"
	"log"
	"net/http"
)

// TODO come up with a way to store this better, maybe a database
var parties []Party


func main() {

	router := mux.NewRouter()

	// routes for modifying parties
	router.HandleFunc("/party/", GetParties).Methods("GET").Queries("id", "{id}")
	router.HandleFunc("/party/{name}", GetParty).Methods("GET").Queries("id","{id}")
	router.HandleFunc("/party/{name}", TryGetParty).Methods("HEAD").Queries("id","{id}")
	router.HandleFunc("/party/{name}", CreateParty).Methods("POST").Queries("id","{id}")
	router.HandleFunc("/party/{name}", DeleteParty).Methods("DELETE").Queries("id","{id}")

	// routes for modifying songs
	router.HandleFunc("/party/{name}/{songId}", CreatePartySong).Methods("POST").
	Queries("id","{id}").
	Queries("title","{title}").
	Queries("imageUrl", "{imageUrl}")
	
	router.HandleFunc("/party/{name}/{songId}", DeletePartySong).Methods("DELETE").Queries("id","{id}")

	// routes for voting on songs
	router.HandleFunc("/party/{name}/{songId}/upvote", UpvotePartySong).Methods("POST").Queries("id", "{id}")
	router.HandleFunc("/party/{name}/{songId}/upvote", UndoUpvotePartySong).Methods("DELETE").Queries("id","{id}")

	router.HandleFunc("/party/{name}/{songId}/downvote", DownvotePartySong).Methods("POST").Queries("id","{id}")
	router.HandleFunc("/party/{name}/{songId}/downvote", UndoDownvotePartySong).Methods("DELETE").Queries("id","{id}")

	log.Fatal(http.ListenAndServe(":8080", router))

}

// returns true if the party name is already on the server
func partyExists(partyName string) bool {
	for _, item := range parties {
		if item.Name == partyName {
			return true
		}
	}
	return false
}


// gets all parties
func GetParties(w http.ResponseWriter, r *http.Request)  {

	json.NewEncoder(w).Encode(parties)
}


// gets party specified by name
func GetParty(w http.ResponseWriter, r *http.Request)    {

	params := mux.Vars(r)

	if partyExists(params["name"]) { 
		for _, item := range parties {
			if item.Name == params["name"] {
				json.NewEncoder(w).Encode(item)
			}
		}
		w.WriteHeader(200)
	} else {
		// party does not exist
		w.WriteHeader(404)
	}
}

// tests if a party exists 
// returns 404 if party not found
// returns 200 if party exists
func TryGetParty(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	// id := r.FormValue("id")

	if partyExists(params["name"]) {
		// ok, the party exists
		w.WriteHeader(200)
	} else {
		// forbidden there is already a party with this name
		w.WriteHeader(404)
	}
}


// creates a party by name
func CreateParty(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	id := r.FormValue("id")
	// check to make sure the party does not already exists
	if partyExists(params["name"]) {
		// forbidden there is already a party with this name
		w.WriteHeader(403)
	} else {
		// TODO add creation date to party
		parties = append(parties, Party{Name: params["name"], Creator: id})
		w.WriteHeader(201)
	}
}


// deletes a party by name
func DeleteParty(w http.ResponseWriter, r *http.Request) {
	id := r.FormValue("id")
	params := mux.Vars(r)
	for index, item := range parties {
		if item.Name == params["name"] {

			if id != item.Creator {
				// return forbidden if user tries to delete party that isnt his
				w.WriteHeader(403);
				return
			}
			parties = append(parties[:index], parties[index+1:]...)
			w.WriteHeader(200)
			return
		}
	}
	w.WriteHeader(404)
}



// returns true if a party and song already exists on this server
func songExists(partyName string, songId string) bool {
	for i, item := range parties {
		if item.Name == partyName {
			for _, song := range parties[i].Songs {
				if song.Id == songId {
					return true;
				}
			}
		}
	}
	return false;
}


// creates a party song by party name and song id
// response returns ok if upvoted 
func CreatePartySong(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	id := r.FormValue("id")
	title := r.FormValue("title")
	imageUrl := r.FormValue("imageUrl")

	for i, item := range parties {
		if item.Name == params["name"] {

			// check if the song already exists and upvote if it does
			if songExists(params["name"], params["songId"]) {
				UpvotePartySong(w, r)
				w.WriteHeader(200)
				return
			} else {
				parties[i].Songs = append(item.Songs, Song{Uploader: id, Id: params["songId"], Title: title, ImageUrl: imageUrl, Upvotes: 0, Downvotes: 0})
				w.WriteHeader(201)
				return
			}
		}
	}
	w.WriteHeader(404)
}


// deletes a party song by party name and song id
func DeletePartySong(w http.ResponseWriter, r *http.Request) {

	id := r.FormValue("id")

	params := mux.Vars(r)
	for i, item := range parties {
		if item.Name == params["name"] {
			for j, song := range parties[i].Songs {
				if song.Id == params["songId"] {
					
					// check if user making request has permission to delete song
					if id != parties[i].Creator && id != song.Uploader{
						w.WriteHeader(403)
						return
					}

					parties[i].Songs = append(parties[i].Songs[:j], parties[i].Songs[j+1:]...)
					w.WriteHeader(200)
					return
				}
			}
		}
	}
	w.WriteHeader(404)
}


// these next 4 methods arent good solutions, too much reused code
// TODO redesign this by passing functions as parameters

// upvotes a song by party name and sond id
func UpvotePartySong(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	for i, item := range parties {
		if item.Name == params["name"] {
			for j, song := range parties[i].Songs {
				if song.Id == params["songId"] {
					parties[i].Songs[j].Upvotes += 1
					w.WriteHeader(200)
					return
				}
			}
		}
	}
	w.WriteHeader(404)
}
// undo upvotes a song by party name and sond id
func UndoUpvotePartySong(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	for i, item := range parties {
		if item.Name == params["name"] {
			for j, song := range parties[i].Songs {
				if song.Id == params["songId"] {
					parties[i].Songs[j].Upvotes -= 1
					w.WriteHeader(200)
					return
				}
			}
		}
	}
	w.WriteHeader(404)
}

// downvotes a song by party name and sond id
func DownvotePartySong(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	for i, item := range parties {
		if item.Name == params["name"] {
			for j, song := range parties[i].Songs {
				if song.Id == params["songId"] {
					parties[i].Songs[j].Downvotes += 1
					w.WriteHeader(200)
					return
				}
			}
		}
	}
	w.WriteHeader(404)
}

// undo downvote to a song by party name and sond id
func UndoDownvotePartySong(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	for i, item := range parties {
		if item.Name == params["name"] {
			for j, song := range parties[i].Songs {
				if song.Id == params["songId"] {
					parties[i].Songs[j].Downvotes -= 1
					w.WriteHeader(200)
					return
				}
			}
		}
	}
	w.WriteHeader(404)
}


type Party struct {
	Name  string   `json:"name"`
	Creator string `json:"creator"`
	// TODO add a field for creation date
	Songs []Song `json:"songs"`
}
type Song struct {
	Uploader string `json:"uploader"`
	Id string `json:"id"`
	Title string `json:"title"`
	ImageUrl string `json:"imageUrl"`
	Upvotes int `json:"upvotes"`
	Downvotes int `json:"downvotes"`
}
