
#include <map>
#ifndef BOOST_ECHO_CLIENT_STOMPFRAME_H
#define BOOST_ECHO_CLIENT_STOMPFRAME_H
using namespace std;

enum Command{
    CONNECT,CONNECTED,MESSAGE,RECEIPT,ERROR,SEND,SUBSCRIBE,UNSUBSCRIBE,DISCONNECT
};

class Frame{
public:
    Frame();
    Command getCommand() const;

    void setCommand(Command commend);

    virtual ~Frame();

    map<string, string> getHeaders() ;

    void setHeaders(const map<string, string> &headers);

    const string &getBody() const;

    void setBody(const string &body);

    void addHeader(string head, string msg);

    void parse(string message);

    std::string toString();

    string toString2();

private:
    Command command;
    std::map<string,string> headers;
    std::string  body;
    std::map<string, Command> strToCommand;
    std::map<Command, string> commandToString;


};
#endif
