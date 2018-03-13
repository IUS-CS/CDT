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
		router.HandleFunc("/party", GetParties).Methods("GET")
		router.HandleFunc("/party/{name}", GetParty).Methods("GET")
		router.HandleFunc("/party/{name}", CreateParty).Methods("POST")
		router.HandleFunc("/party/{name}", DeleteParty).Methods("DELETE")
		log.Fatal(http.ListenAndServe(":8080", router))




}

// gets all parties
func GetParties(w http.ResponseWriter, r *http.Request)  {
	json.NewEncoder(w).Encode(parties)
}

func GetParty(w http.ResponseWriter, r *http.Request)    {
params := mux.Vars(r)
			for _, item := range parties {
				if item.Name == params["name"] {
					json.NewEncoder(w).Encode(item)
				}
			}
}
func CreateParty(w http.ResponseWriter, r *http.Request) {
params := mux.Vars(r)
			parties = append(parties, Party{Name: params["name"], })
			json.NewEncoder(w).Encode(parties)
}


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

