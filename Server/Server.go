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

	//TODO these parties are just for testing. need to create parties dynamically
	parties = append(parties, Party{Name: "FastArrow", Song: &Song{Title: "Dusk Till Dawn", Id: "tt2k8PGm-TI", Img: "States://i.ytimg.com/vi/tt2k8PGm-TI/hqdefault.jpg?sqp=-oaymwEXCPYBEIoBSFryq4qpAwkIARUAAIhCGAE=&rs=AOn4CLAMjoIH9ewItfPK4HJ-Bse6k2ZM5w", Upvotes: "5", Downvotes: "1"}})
		parties = append(parties, Party{Name: "Waterfront", Song: &Song{Title: "Dusk Till Dawn", Id: "tt2k8PGm-TI", Img: "States://i.ytimg.com/vi/tt2k8PGm-TI/hqdefault.jpg?sqp=-oaymwEXCPYBEIoBSFryq4qpAwkIARUAAIhCGAE=&rs=AOn4CLAMjoIH9ewItfPK4HJ-Bse6k2ZM5w", Upvotes: "5", Downvotes: "1"}})
		parties = append(parties, Party{Name: "abc", Song: &Song{Title: "Dusk Till Dawn", Id: "tt2k8PGm-TI", Img: "States://i.ytimg.com/vi/tt2k8PGm-TI/hqdefault.jpg?sqp=-oaymwEXCPYBEIoBSFryq4qpAwkIARUAAIhCGAE=&rs=AOn4CLAMjoIH9ewItfPK4HJ-Bse6k2ZM5w", Upvotes: "6", Downvotes: "3"}})
		parties = append(parties, Party{Name: "dude", Song: &Song{Title: "Dusk Till Dawn", Id: "tt2k8PGm-TI", Img: "States://i.ytimg.com/vi/tt2k8PGm-TI/hqdefault.jpg?sqp=-oaymwEXCPYBEIoBSFryq4qpAwkIARUAAIhCGAE=&rs=AOn4CLAMjoIH9ewItfPK4HJ-Bse6k2ZM5w", Upvotes: "6", Downvotes: "3"}})
		parties = append(parties, Party{Name: "party", Song: &Song{Title: "Dusk Till Dawn", Id: "tt2k8PGm-TI", Img: "States://i.ytimg.com/vi/tt2k8PGm-TI/hqdefault.jpg?sqp=-oaymwEXCPYBEIoBSFryq4qpAwkIARUAAIhCGAE=&rs=AOn4CLAMjoIH9ewItfPK4HJ-Bse6k2ZM5w", Upvotes: "6", Downvotes: "3"}})
		parties = append(parties, Party{Name: "hello", Song: &Song{Title: "Dusk Till Dawn", Id: "tt2k8PGm-TI", Img: "States://i.ytimg.com/vi/tt2k8PGm-TI/hqdefault.jpg?sqp=-oaymwEXCPYBEIoBSFryq4qpAwkIARUAAIhCGAE=&rs=AOn4CLAMjoIH9ewItfPK4HJ-Bse6k2ZM5w", Upvotes: "6", Downvotes: "3"}})
		parties = append(parties, Party{Name: "doggo", Song: &Song{Title: "Dusk Till Dawn", Id: "tt2k8PGm-TI", Img: "States://i.ytimg.com/vi/tt2k8PGm-TI/hqdefault.jpg?sqp=-oaymwEXCPYBEIoBSFryq4qpAwkIARUAAIhCGAE=&rs=AOn4CLAMjoIH9ewItfPK4HJ-Bse6k2ZM5w", Upvotes: "6", Downvotes: "3"}})

		router := mux.NewRouter()

		// routes for modifying parties
		router.HandleFunc("/party", GetParties).Methods("GET")
		router.HandleFunc("/party/{name}", GetParty).Methods("GET")
		router.HandleFunc("/party/{name}", CreateParty).Methods("POST")
		router.HandleFunc("/party/{name}", DeleteParty).Methods("DELETE")

		// routes for modifying songs
		router.HandleFunc("/party/{name}/{songId}", GetPartySong).Methods("GET")
		router.HandleFunc("/party/{name}/{songId}", CreatePartySong).Methods("POST")
		router.HandleFunc("/party/{name}/{songId}", DeletePartySong).Methods("DELETE")

		// routes for voting on songs
		router.HandleFunc("/party/{name}/{songId}/vote", UpvotePartySong).Methods("POST")
		router.HandleFunc("/party/{name}/{songId}/vote", DownvotePartySong).Methods("DELETE")

		log.Fatal(http.ListenAndServe(":8080", router))

}

// gets all parties
func GetParties(w http.ResponseWriter, r *http.Request)  {
	json.NewEncoder(w).Encode(parties)
}


// gets party specified by name
func GetParty(w http.ResponseWriter, r *http.Request)    {
params := mux.Vars(r)
			for _, item := range parties {
				if item.Name == params["name"] {
					json.NewEncoder(w).Encode(item)
				}
			}
}


// creates a party by name
func CreateParty(w http.ResponseWriter, r *http.Request) {
params := mux.Vars(r)
			parties = append(parties, Party{Name: params["name"], })
			json.NewEncoder(w).Encode(parties)
}


// deletes a party by name
func DeleteParty(w http.ResponseWriter, r *http.Request) {
params := mux.Vars(r)
			for index, item := range parties {
				if item.Name == params["name"] {
					parties = append(parties[:index], parties[index+1:]...)
						break
				}
			}
		json.NewEncoder(w).Encode(parties)

}


// gets a party song by party name and song id
// TODO
func GetPartySong(w http.ResponseWriter, r *http.Request) {

	}
	

// creates a party song by party name and song id
// TODO
func CreatePartySong(w http.ResponseWriter, r *http.Request) {

	}
	

// deletes a party song by party name and song id
// TODO
func DeletePartySong(w http.ResponseWriter, r *http.Request) {

	}


// upvotes a song by party name and sond id
// TODO
func UpvotePartySong(w http.ResponseWriter, r *http.Request) {

	}

// downvotes a song by party name and sond id
// TODO
func DownvotePartySong(w http.ResponseWriter, r *http.Request) {

	}



// this is for test testing
func Sum(x int, y int) int {
	return x + y
}

type Party struct {
	Name  string   `json:"name,omitempty"`
		// TODO make this be an array of songs
		Song   *Song `json:"Song,omitempty"`
}
type Song struct {
	Title  string `json:"title,omitempty"`
		Id string `json:"id,omitempty"`
		Img string `json:"img,omitempty"`
		Upvotes string `json:"upvotes,omitempty"`
		Downvotes string `json:"downvotes,omitempty"`
}

