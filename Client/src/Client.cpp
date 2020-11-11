#include <boost/algorithm/string.hpp>
#include <vector>
#include <iostream>
#include <thread>
#include "../include/Frame.h"
#include "../include/ConnectionHandler.h"
#include "../include/Processor.h"
#include "../include/Protocol.h"
#include "../include/ConnectThread.h"

using namespace std;

/**
* This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
*/
int main(int argc, char *argv[])
{

    std::string input;
    UserBookData user;
    ConnectionHandler *pHandler = nullptr;
    Protocol *pProtocol = nullptr;
    Processor processor = Processor(&user);
    while (true) //this is the login msg
    {
        input = "";
        std::getline(std::cin, input);
        if (input.find("login") == std::string::npos)
            std::cout << "Error please login again" << endl;
        else
        {
            std::pair<std::string, short> address = processor.get_host(input);
            pHandler = new ConnectionHandler(address.first, address.second);
            pProtocol = new Protocol(*pHandler, &user);
            if (pHandler->connect())
            {
                pHandler->sendFrameAscii(processor.process(input), '\0');

                std::string response = ""; //server response
                pHandler->getFrameAscii(response, '\0');

                pProtocol->process(response);
                if (pProtocol->get_should_Disconnect() == true)
                {
                    break;
                }
                if (pProtocol->isLoggedIn())
                {
                    user.setName(input);
                    break;
                }
            }
        }
    }
    if (pProtocol->get_should_Disconnect() == false)
    {
        ConnectThread task(*pHandler, *pProtocol);
        std::thread threadMsgFromServer(task);

        while (true && pProtocol->get_should_Disconnect() == false)
        {

            input = "";
            std::getline(std::cin, input);
            std::string msgOut = processor.process(input);
            pHandler->sendFrameAscii(msgOut, '\0');
            if ((input.find("logout") != std::string::npos))
            {
                break;
            }
        }

        threadMsgFromServer.join();
    }

    delete pHandler;
    delete pProtocol;
    return 0;
}