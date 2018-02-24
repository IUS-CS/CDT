package main

import (
	"fmt"
	"log"
	"net/http"
)

func main() {
	router := NewRouter()

	log.Fatal(http.ListenAndServe(":8080", router))
}

func Sum(x int, y int) int {
	return x + y
}
