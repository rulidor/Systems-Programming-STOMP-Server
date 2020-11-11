
#include "../include/ConnectThread.h"
#include "../include/Protocol.h"

ConnectThread::ConnectThread(ConnectionHandler &handler1, Protocol &protocol1): handler(handler1), protocol(protocol1) {}

void ConnectThread::operator()() {
    std::string msg;
    do
    {
        msg="";
        handler.getFrameAscii(msg, '\0');
        protocol.process(msg);
        if(!protocol.isLoggedIn()) {
            //connectionHandler.close();
            break;
        }
    }while(true);

}