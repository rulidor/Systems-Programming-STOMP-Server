CFLAGS:=-c -Wall -Weffc++ -g -std=c++11  -Iinclude -pthread
LDFLAGS:=-lboost_system -pthread


all: StompBookClubClient
	g++ -o bin/StompBookClubClient bin/ConnectionHandler.o bin/Client.o bin/Processor.o bin/Frame.o bin/UserBookData.o bin/ConnectThread.o bin/Protocol.o $(LDFLAGS)

StompBookClubClient: bin/ConnectionHandler.o bin/Client.o bin/Processor.o bin/Frame.o bin/UserBookData.o bin/ConnectThread.o bin/Protocol.o

bin/ConnectionHandler.o: src/ConnectionHandler.cpp
	g++ $(CFLAGS) -o bin/ConnectionHandler.o src/ConnectionHandler.cpp

bin/Client.o: src/Client.cpp
	g++ $(CFLAGS) -o bin/Client.o src/Client.cpp

bin/Processor.o: src/Processor.cpp
	g++ $(CFLAGS) -o bin/Processor.o src/Processor.cpp

bin/Frame.o: src/Frame.cpp
	g++ $(CFLAGS) -o bin/Frame.o src/Frame.cpp

bin/UserBookData.o: src/UserBookData.cpp
	g++ $(CFLAGS) -o bin/UserBookData.o src/UserBookData.cpp

bin/ConnectThread.o: src/ConnectThread.cpp
	g++ $(CFLAGS) -o bin/ConnectThread.o src/ConnectThread.cpp

bin/Protocol.o: src/Protocol.cpp
	g++ $(CFLAGS) -o bin/Protocol.o src/Protocol.cpp

.PHONY: clean
clean:
	rm -f bin/*
