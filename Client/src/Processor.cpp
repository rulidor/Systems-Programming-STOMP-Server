
#include <iterator>
#include <sstream>
#include "../include/Processor.h"
#include "../include/Frame.h"

using namespace std;

Processor::Processor(UserBookData *_user) : subsIDCounter(1), receiptIdCounter(1), user(_user){};

std::pair<std::string, short> Processor::get_host(std::string input)
{
    int index = input.find(':');
    std::string host = input.substr(6, index - 6);
    short port = std::stoi(input.substr(index + 1, 4));
    return std::make_pair(host, port);
}
std::string Processor::process(string input)
{
    std::istringstream iss(input);
    std::vector<std::string> res(std::istream_iterator<std::string>{iss},
                                 std::istream_iterator<std::string>());
    std::string answer;
    if (res[0] == "login")
        answer = login(res);
    else if (res[0] == "join")
        answer = subscribe(res);
    else if (res[0] == "status")
        answer = status(res);
    else if (res[0] == "add")
        answer = add(res);
    else if (res[0] == "borrow")
        answer = borrow(res);
    else if (res[0] == "return")
        answer = returnBook(res);
    else if (res[0] == "logout")
        answer = logout(res);
    else if (res[0] == "exit")
        answer = exit(res);

    return answer;
}

std::string Processor::login(vector<string> &letters)
{
    Frame frame = Frame();
    frame.setCommand(CONNECT);
    frame.addHeader("accept-version", "1.2");
    int index = letters[1].find(':');
    frame.addHeader("host", letters[1].substr(0, index));
    frame.addHeader("login", letters[2]);
    frame.addHeader("passcode", letters[3]);
    return frame.toString();
}

std::string Processor::subscribe(vector<string> &letters)
{
    Frame *frame = new Frame();
    frame->setCommand(SUBSCRIBE);
    frame->addHeader("destination", letters[1]);
    frame->addHeader("id", to_string(subsIDCounter));
    frame->addHeader("receipt", to_string(receiptIdCounter));
    user->addSubscribe(letters[1], subsIDCounter);
    user->addReceipt(receiptIdCounter, frame);
    subsIDCounter++;
    receiptIdCounter++;
    return frame->toString();
}

std::string Processor::exit(vector<string> &letters)
{
    Frame *frame = new Frame();
    frame->setCommand(UNSUBSCRIBE);
    frame->addHeader("id", to_string(user->getSubsID(letters[1])));
    frame->addHeader("receipt", to_string(receiptIdCounter));
    user->addReceipt(receiptIdCounter, frame);
    receiptIdCounter++;
    return frame->toString();
}

std::string Processor::add(std::vector<std::string> &letters)
{
    Frame frame = Frame();
    frame.setCommand(SEND);
    frame.addHeader("destination", letters[1]);
    std::string book = bookName(letters, 2, letters.size());
    frame.setBody(user->getUserName() + " has added the book " + book);
    user->addBook(letters[1], book);
    return frame.toString();
}

std::string Processor::borrow(std::vector<std::string> &letters)
{
    Frame frame = Frame();
    frame.setCommand(SEND);
    frame.addHeader("destination", letters[1]);
    std::string book = bookName(letters, 2, letters.size());
    frame.setBody(user->getUserName() + " wish to borrow " + book);
    user->wishToBorrow(book);
    return frame.toString();
}

std::string Processor::returnBook(std::vector<std::string> &letters)
{
    Frame frame = Frame();
    frame.setCommand(SEND);
    frame.addHeader("destination", letters[1]);
    std::string book = bookName(letters, 2, letters.size());
    frame.setBody("Returning " + book + " to " + user->getLoanerName(book));
    user->deleteBook(letters[1], book);
    return frame.toString();
}

std::string Processor::status(std::vector<std::string> &letters)
{
    Frame frame = Frame();
    frame.setCommand(SEND);
    frame.addHeader("destination", letters[1]);
    frame.setBody("book status");
    return frame.toString();
}

std::string Processor::logout(std::vector<std::string> &letters)
{ // check if to send unsbsibe to each topic
    Frame frame = Frame();
    frame.setCommand(DISCONNECT);
    frame.addHeader("receipt", to_string(receiptIdCounter));
    user->addReceipt(receiptIdCounter, &frame);
    //    receiptIdCounter++;
    return frame.toString();
}

std::string Processor::bookName(std::vector<std::string> letters, int start, int end)
{
    std::string book;
    for (int i = start; i < end; i++)
    {
        book += letters[i] + " ";
    }
    return book.substr(0, book.size() - 1);
}