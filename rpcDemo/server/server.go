package main

import (
	"log"
	"net"
	"net/rpc"
	"os"
	"time"
)

var userTable map[string]string

type User struct {
	username string
	passwd   string
}

type GetInfo int

//rpc method
func (t *GetInfo) GetServerCurrntTime(args *User, reply *time.Time) error {

	*reply = time.Now().Local()
	return nil
}

func authorize() bool {

	return true
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
	userTable = make(map[string]string)
	userTable["error"] = "error"
	userTable["gao"] = "shao"
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
		rpc.ServeConn(conn)
	}
}
