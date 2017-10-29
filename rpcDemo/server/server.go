package main

import (
	"encoding/json"
	"errors"
	"log"
	"net"
	"net/rpc"
	"os"
	"time"
)

var userTable map[string]string

type User struct {
	Username string
	Passwd   string
}

type GetInfo int


//rpc method
func (t *GetInfo) GetServerCurrntTime(args *User, reply *time.Time) error {

	ok, tips := authorize(*args)
	if ok {
		*reply = time.Now().Local()
		//fmt.Println(*reply)
		return nil
	} else {
		return errors.New(tips)
	}

}

func authorize(u User) (bool, string) {

	//log.Println(u.Username, "for authorizatio")

	passwd, ok := userTable[u.Username]
	if ok {
		if passwd == u.Passwd {
			return true, ""
		} else {
			return false, "wrong password!"
		}
	} else {
		return false, u.Username + " does not exist!"
	}
}

func Init() {
	//log init
	logFile, err := os.OpenFile("./server.log", os.O_CREATE|os.O_WRONLY|os.O_APPEND, 0666)
	if err != nil {
		log.Fatal("OpenFile ERROR:", err)
	}
	log.SetFlags(log.Ldate | log.Ltime | log.Lshortfile)
	log.SetOutput(logFile)

	//userTable init
	userFile, err := os.OpenFile("./user.json", os.O_RDONLY, 0666)
	if err != nil {
		log.Fatal("OpenFile ERROR:", err)
	}
	decoder := json.NewDecoder(userFile)
	user := User{}
	userTable = make(map[string]string)
	for decoder.More() {
		err = decoder.Decode(&user)
		if err != nil {
			log.Fatal("confige file decode ERROR :", err)
		}
		userTable[user.Username] = user.Passwd
	}

}

func main() {

	Init()

	getinfo := new(GetInfo)
	var err = rpc.Register(getinfo)
	if err != nil {
		log.Fatal("rpc.Register() ERROR :", err)
	}

	tcpAddr, err := net.ResolveTCPAddr("tcp", ":6789")
	if err != nil {
		log.Fatal("ResolveTCPAddr ERROR :", err)
	}

	listen, err := net.ListenTCP("tcp", tcpAddr)
	if err != nil {
		log.Fatal("ListenTCP ERROR :", err)
	}

	for {
		conn, err := listen.Accept()
		if err != nil {
			log.Println("listen.Accept() :", err)
			continue
		}
		log.Println("accept ip : ", conn.RemoteAddr())
		go rpc.ServeConn(conn)
	}
}
