
#include "../include/Frame.h"
#include <map>
#include <sstream>
#include <vector>

using namespace std;

Frame::Frame() : command(), headers(std::map<string, string>()), body(""), strToCommand(std::map<string, Command>()), commandToString(std::map<Command, string>())
{

    commandToString.insert({CONNECT, "CONNECT"});
    commandToString.insert({SUBSCRIBE, "SUBSCRIBE"});
    commandToString.insert({UNSUBSCRIBE, "UNSUBSCRIBE"});
    commandToString.insert({SEND, "SEND"});
    commandToString.insert({DISCONNECT, "DISCONNECT"});
    strToCommand.insert({"CONNECTED", CONNECTED});
    strToCommand.insert({"RECEIPT", RECEIPT});
    strToCommand.insert({"MESSAGE", MESSAGE});
    strToCommand.insert({"ERROR", ERROR});
}

Command Frame::getCommand() const
{
    return command;
}

void Frame::setCommand(Command commend)
{
    this->command = commend;
}

map<string, string> Frame::getHeaders()
{
    return headers;
}

void Frame::setHeaders(const map<string, string> &headers)
{
    this->headers = headers;
}

const string &Frame::getBody() const
{
    return body;
}

void Frame::addHeader(string head, string msg)
{
    this->headers.insert({head, msg});
}

void Frame::parse(string message)
{
    if (message == "")
        return;
    else
    {
        vector<string> lines;
        std::istringstream stream(message);
        std::string line; // to parse the msg - every line
        while (getline(stream, line))
        {
            lines.push_back(line);
        }
        this->command = strToCommand[lines[0]];
        int i = 1;

        while ((unsigned)i < lines.size() && lines[i] != "")
        {
            int count = lines[i].find(':');
            addHeader(lines[i].substr(0, count), lines[i].substr(count + 1, lines[i].size()));
            i++;
        }

        if ((unsigned)i < lines.size() - 1)
            body = lines[i + 1];
        else
            body = "";
    }
}
std::string Frame::toString()
{
    string out;
    out = commandToString[command] + "\n";
    for (auto head : headers)
    {
        out = out + head.first + ":" + head.second + "\n";
    }
    out = out + "" + "\n";
    out += body;
    return out;
}
std::string Frame::toString2()
{
    string out;
    out = commandToString[command] + "\n";

    out = out + "" + "\n";
    out += body;
    return out;
}

void Frame::setBody(const string &body)
{
    Frame::body = body;
}

Frame::~Frame(){

};
