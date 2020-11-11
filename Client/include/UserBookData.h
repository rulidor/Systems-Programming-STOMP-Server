

#ifndef BOOST_ECHO_CLIENT_USERBOOKDATA_H
#define BOOST_ECHO_CLIENT_USERBOOKDATA_H

#include <map>
#include <vector>
#include <mutex>
#include "Frame.h"

class UserBookData{

public:
    UserBookData();
    void addSubscribe(std::string topic, int subid);
    void deleteSubscribe(std::string topic);
    void addBook(std::string topic, std::string book);
    void addReceipt(int receiptID, Frame* frame);
    int getSubsID(std::string topic);
    std::string getUserName();
    void wishToBorrow(std::string book);
    void deleteBook(std::string genre, std::string book);
    std::string getLoanerName(std::string book);
    Frame &getReceiptFrame(int id);
    void deleteReceipt(int id);
    bool isBookExistInTopic(std::string genre, std::string book);
    bool isWantedToBeBorrowed(std::string book);
    void borrowBook(std::string genre, std::string book, std::string nameLoner);
    std::string printBooks(std::string genre);
    void setName(std::string new_name);
    ~UserBookData();
    void lendBook(std::string topic,std::string book);
    std::string getTopicBySubsID(int id);

private:
    std::map<std::string,std::vector<std::string>> booksByTopic; // key: topic. value: books related
    std::map<std::string,std::string> loanerByBook; // key: bookName, value: userName of the loaner
    std::map<std::string, int> topicBySubsID;
public:
    const map<std::string, int> &getTopicBySubsId() const;

private:
    std::vector<std::string> booksWantToBorrow;
    std::map<int, Frame*> receiptsByID;
    std::string userName;
    std:: mutex topicBooksLock;
    std:: mutex borrowLock;
    std:: mutex receiptLock;
};

#endif
