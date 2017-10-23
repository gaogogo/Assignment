package main

import (
	"log"
	"net/rpc"
	"time"
)

type User struct {
	username string
	passwd   string
}

func main() {

	client, err := rpc.Dial("tcp", "localhost:6789")
	if err != nil {
		log.Fatal("dialing ERROR :", err)
	}

	args := User{username: "error", passwd: "error"}

	var gettime time.Time
	TBegin := time.Now().Local()
	err = client.Call("GetInfo.GetServerCurrntTime", args, &gettime)
	duration := time.Since(TBegin)
	log.Println(gettime.Format("2006-01-02 15:04:05:"), gettime.Nanosecond())
	gettime = gettime.Truncate(duration / 2)

	if err != nil {
		log.Fatal("GetInfo error: ", err)
	}

	log.Println(gettime.Format("2006-01-02 15:04:05"), gettime.Nanosecond())
}
