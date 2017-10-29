package main

import (
	"encoding/json"
	"log"
	"net/rpc"
	"os"
	"time"
)

type User struct {
	Username string
	Passwd   string
}

type Configuration struct {
	Server   string
	Username string
	Passwd   string
}

func main() {

	log.SetFlags(log.Ltime)

	confFile, err := os.OpenFile("./conf.json", os.O_RDONLY, 0666)
	if err != nil {
		log.Fatal("OpenFile ERROR:", err)
	}

	decoder := json.NewDecoder(confFile)
	conf := Configuration{}
	err = decoder.Decode(&conf)
	if err != nil {
		log.Fatal("confige file decode ERROR :", err)
	}

	client, err := rpc.Dial("tcp", conf.Server)
	if err != nil {
		log.Fatal("dialing ERROR :", err)
	}

	args := User{conf.Username, conf.Passwd}

	var gettime time.Time
	for true {
		TBegin := time.Now().Local()
		err = client.Call("GetInfo.GetServerCurrntTime", args, &gettime)
		duration := time.Since(TBegin)

		if err != nil {
			log.Fatal("GetInfo error: ", err)
		}

		gettime = gettime.Truncate(duration / 2)
		log.Println(gettime.Format("2006-01-02 15:04:05"), "+", gettime.Nanosecond(), "ns")
		time.Sleep(time.Second)
	}


	confFile.Close()
}
