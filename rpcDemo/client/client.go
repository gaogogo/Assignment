package main

import (
	"log"
	"net/rpc"
	"time"
)

type User struct {
	Username string
	Passwd   string
}

func main() {

	client, err := rpc.Dial("tcp", "localhost:6789")
	if err != nil {
		log.Fatal("dialing ERROR :", err)
	}

	args := User{"error", "errors"}

	var gettime time.Time
	TBegin := time.Now().Local()
	err = client.Call("GetInfo.GetServerCurrntTime", args, &gettime)
	duration := time.Since(TBegin)

	if err != nil {
		log.Fatal("GetInfo error: ", err)
	}

	gettime = gettime.Truncate(duration / 2)
	log.Println(gettime.Format("2006-01-02 15:04:05"), gettime.Nanosecond())
}
