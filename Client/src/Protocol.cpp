#include "../include/Protocol.h"
#include "../include/UserBookData.h"
#include "../include/Frame.h"

using namespace std;

Protocol::Protocol(ConnectionHandler &handler1, UserBookData *user1) : connectionHandler(handler1), islogOut(false), login(false), user(user1), shouldDisconnect(false){};

bool Protocol::get_should_Disconnect()
{
    return this->shouldDisconnect;
}

void Protocol::process(std::string msg)
{
    Frame frame = Frame();
    frame.parse(msg);

    if (frame.getCommand() == CONNECTED)
        connected();
    else if (frame.getCommand() == ERROR)
    {
        errorMsg(frame.getHeaders()["msg"]);
        std::cout << "Disconnected" << endl;
        this->shouldDisconnect = true;
        login = false;
    }
    else if (frame.getCommand() == RECEIPT)
    {
        if (frame.getHeaders().size() == 0)
        {
            std::cout << "Disconnected" << endl;
            login = false;
        }
        else
            receipt(frame.getHeaders()["receipt-id"]);
    }
    else if (frame.getCommand() == MESSAGE)
        messageProtocol(frame);
    /* else if (frame.getCommand()==DISCONNECT)
        receipt(frame.getHeaders()["receipt-id"]);*/
}

void Protocol::connected()
{
    std::cout << "Login successful" << endl;
    login = true;
}

void Protocol::errorMsg(std::string msg)
{
    std::cout << msg;
    login = false;
}

void Protocol::receipt(std::string msg)
{
    int receiptID = std::stoi(msg);
    Frame *frame = &user->getReceiptFrame(receiptID);
    if (frame->getCommand() == SUBSCRIBE)
    {
        std::cout << "Joined club " << frame->getHeaders()["destination"] << endl;
    }

    else if (frame->getCommand() == UNSUBSCRIBE)
    {
        int subid = std::stoi(frame->getHeaders()["id"]);
        //std::cout << "Exited club " << user->getTopicBySubsID(subid) << endl;
        for (auto ite : user->getTopicBySubsId())
        {
            if (ite.second == subid)
            {
                std::string res = ite.first;
                std::cout << "Exited club " << res << endl;
                break;
            }
        }
    }

    frame = nullptr;
}

void Protocol::messageProtocol(Frame frame)
{
    std::istringstream iss(frame.getBody());
    std::vector<std::string> results(std::istream_iterator<std::string>{iss},
                                     std::istream_iterator<std::string>());
    if ((results[0] == user->getUserName()) | (results.size() == 1))
    {
        std::cout << frame.getBody() << std::endl;
    }
    else if (results[1] == "wish") //case: got a "wish" request - search for the book
    {
        std::string nameOfBook = bookName(results, 4, results.size());
        if (user->isBookExistInTopic(frame.getHeaders()["destination"], nameOfBook))
        {
            Frame frameToSend = Frame();
            frameToSend.setCommand(SEND);
            frameToSend.addHeader("destination", frame.getHeaders()["destination"]);
            frameToSend.setBody(user->getUserName() + " has " + nameOfBook);
            connectionHandler.sendFrameAscii(frameToSend.toString(), '\0');
        }
        std::cout << frame.getBody() << std::endl;
    }

    else if (results[1] == "has")
    {
        std::string nameOfBook;
        if (results[2] == "added")
            nameOfBook = bookName(results, 5, results.size());
        else
            nameOfBook = bookName(results, 2, results.size());

        if (user->isWantedToBeBorrowed(nameOfBook))
        {
            user->borrowBook(frame.getHeaders()["destination"], nameOfBook, results[0]);
            Frame send = Frame();
            send.setCommand(SEND);
            send.addHeader("destination", frame.getHeaders()["destination"]);
            send.setBody("Taking " + nameOfBook + " from " + results[0]);
            connectionHandler.sendFrameAscii(send.toString(), '\0');
        }
        std::cout << frame.getBody() << std::endl;
    }
    else if ((results[0] == "Taking") & (results[results.size() - 1] == user->getUserName())) //check 2 threads try to take the same book
    {
        std::string nameOfBook = bookName(results, 1, results.size() - 2);
        user->lendBook(frame.getHeaders()["destination"], nameOfBook);
        std::cout << frame.getBody() << std::endl;
    }
    else if ((results[0] == "Returning") & (results[results.size() - 1] == user->getUserName()))
    {
        std::string nameOfBook = bookName(results, 1, results.size() - 2);
        user->addBook(frame.getHeaders()["destination"], nameOfBook);
        std::cout << frame.getBody() << std::endl;
    }
    else if (results[1] == "status")
    {
        Frame frameToSend = Frame();
        frameToSend.setCommand(SEND);
        frameToSend.addHeader("destination", frame.getHeaders()["destination"]);
        frameToSend.setBody(user->printBooks(frame.getHeaders()["destination"]));
        connectionHandler.sendFrameAscii(frameToSend.toString(), '\0');
        std::cout << frame.getBody() << std::endl;
    }
    else
    {
        std::cout << frame.getBody() << std::endl;
    }
}

bool Protocol::isLoggedIn() { return login; }

std::string Protocol::bookName(std::vector<std::string> words, int start, int end)
{
    std::string nameOfBook;
    for (int i = start; i < end; i++)
    {
        nameOfBook += words[i] + " ";
    }
    return nameOfBook.substr(0, nameOfBook.size() - 1);
}

bool Protocol::get_is_login()
{
    return login;
}
