

#ifndef BOOST_ECHO_CLIENT_PROCESSOR_H
#define BOOST_ECHO_CLIENT_PROCESSOR_H

#include "UserBookData.h"

class Processor{
public:
    Processor(UserBookData* user);
    std::string process(std::string input);
    static std::pair<std::string, short > get_host(std::string input);

private:
    int subsIDCounter;
    int receiptIdCounter;
    std::string login(std::vector<std::string>& letters);
    std::string subscribe(std::vector<std::string>& letters);
    std::string exit(std::vector<std::string>& letters);
    std::string add(std::vector<std::string>& letters);
    std::string borrow(std::vector<std::string>& letters);
    std::string returnBook(std::vector<std::string>& letters);
    std::string status(std::vector<std::string>& letters);
    std::string logout(std::vector<std::string>& letters);
    std::string bookName(std::vector<std::string> letters, int start, int end);
    UserBookData* user;

};
#endif
