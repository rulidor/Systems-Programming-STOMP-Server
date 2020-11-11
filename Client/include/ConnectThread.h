
#include "ConnectionHandler.h"
#include "Protocol.h"

#ifndef BOOST_ECHO_CLIENT_TASKTHREAD_H
#define BOOST_ECHO_CLIENT_TASKTHREAD_H

class ConnectThread {
public:
    ConnectThread(ConnectionHandler& handler, Protocol& protocol);
    void operator()();
private:
    ConnectionHandler& handler;
    Protocol protocol;
};

#endif
