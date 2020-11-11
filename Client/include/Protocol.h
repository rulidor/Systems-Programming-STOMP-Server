
#include "../include/ConnectionHandler.h"
#include "Frame.h"
#include "UserBookData.h"

#ifndef BOOST_ECHO_CLIENT_PROTOCOL_H
#define BOOST_ECHO_CLIENT_PROTOCOL_H

class Protocol{
public:
    Protocol(ConnectionHandler &handler, UserBookData* user);
    void process(std::string msg);
    bool isLoggedIn();
    bool get_is_login();
    bool get_should_Disconnect();


private:
    ConnectionHandler& connectionHandler;
    bool islogOut;
    bool login;
    bool shouldDisconnect;
    UserBookData* user;
    void connected();
    void errorMsg(std::string msg);
    void receipt(std::string msg);
    void messageProtocol(Frame frame);
    std::string bookName(std::vector<std::string> words, int start, int end);


};
#endif