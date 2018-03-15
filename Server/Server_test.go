package main

import (
	"encoding/json"
	"testing"
//	"fmt"
	"net/http"
	"time"
)


const serverAddress = "solidaycl.com:8080/"



var myClient = &http.Client{Timeout: 10 * time.Second}

func getJson(url string, target interface{}) error {
	r, err := myClient.Get(url)
	if err != nil {
		return err
	}
	defer r.Body.Close()

	return json.NewDecoder(r.Body).Decode(target)
}



type Foo struct {
	Bar string
}


func TestGetParties(t *testing.T) {

	foo1 := new(Foo) // or &Foo{}
	getJson(serverAddress + "/party", foo1)
	t.Errorf(foo1.Bar)

	// parse the json string
	/*
	if err != nil {
		t.Errorf("returned error: " + err.Error())
	}
	*/
}

//TODO test get a party
func TestGetParty(t *testing.T) {
	if true {
		t.Errorf("error msg")	
	}
}


//TODO 
func TestCreateParty(t *testing.T) {
	if true {
		t.Errorf("error msg")	
	}
}

//TODO 
func TestDeleteParty(t *testing.T) {
	if true {
		t.Errorf("error msg")	
	}
}

//TODO create more tests
