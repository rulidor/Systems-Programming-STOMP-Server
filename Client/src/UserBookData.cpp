
#include "../include/UserBookData.h"
#include <algorithm>
#include <sstream>
#include <iterator>
#include <iostream>

using namespace std;

UserBookData::UserBookData(): booksByTopic(map<std::string,std::vector<std::string>>()), loanerByBook(std::map<std::string,std::string>()),
                              topicBySubsID(std::map<std::string, int>()), booksWantToBorrow(), receiptsByID(map<int, Frame*>()) , userName(""), topicBooksLock(), borrowLock(), receiptLock(){};

UserBookData::~UserBookData() {
    for (auto it:receiptsByID)
    {
        it.second = nullptr;
    }
    booksByTopic.clear();
    receiptsByID.clear();
    booksWantToBorrow.clear();
    booksByTopic.clear();
}


void UserBookData::addSubscribe(std::string topic, int subid) {
    std::lock_guard<std::mutex> lock(topicBooksLock); //locks the booksByTopic for other threads
    booksByTopic.insert({topic, std::vector<std::string> ()});
    std::map<std::string,int>::iterator iterator;
    iterator = topicBySubsID.find(topic);
    if (iterator != topicBySubsID.end())
    {
        topicBySubsID[topic]=subid;
    }
    else
    {
        topicBySubsID.insert({topic, subid});
    }
}

void UserBookData::addBook(std::string topic, std::string book) {
    std::lock_guard<std::mutex> lock(topicBooksLock); //locks booksByTopic for other threads
    if(booksByTopic.find(topic) != booksByTopic.end())
        booksByTopic[topic].push_back(book);
    else
    {
        booksByTopic.insert({topic, std::vector<std::string> ()});
        booksByTopic[topic].push_back(book);

    }
}


void UserBookData::addReceipt(int receiptID, Frame *frame) {
    std::lock_guard<std::mutex> lock(receiptLock);
    receiptsByID.insert({receiptID, frame});
}

int UserBookData::getSubsID(std::string topic) {
    return topicBySubsID[topic];
}

std::string UserBookData::getUserName() {return userName;}

void UserBookData::wishToBorrow(std::string book) {
    std::lock_guard<std::mutex> lock(borrowLock);
    booksWantToBorrow.push_back(book);
}

void UserBookData::deleteBook(std::string genre, std::string book) {
    std::cout<<"remove book"<<endl;
    std::lock_guard<std::mutex> lock(topicBooksLock);
    booksByTopic[genre].erase(std::remove(booksByTopic[genre].begin(), booksByTopic[genre].end(), book), booksByTopic[genre].end());

}

std::string UserBookData::getLoanerName(std::string book) {
    return loanerByBook[book];
}

Frame &UserBookData::getReceiptFrame(int id) {
    return *receiptsByID[id];
}

void UserBookData::deleteReceipt(int id) {
    std::lock_guard<std::mutex> lock(receiptLock);
    delete(receiptsByID[id]);
    receiptsByID.erase(id);
}

bool UserBookData::isBookExistInTopic(std::string genre, std::string book) {
    std::lock_guard<std::mutex> lock(topicBooksLock);
    for(auto it:booksByTopic[genre])
    {
        if(it==book)
            return true;
    }
    return false;
}

bool UserBookData::isWantedToBeBorrowed(std::string book) {
    for(auto it: booksWantToBorrow)
    {
        if(it==book)
            return true;
    }
    return false;
}

void UserBookData::borrowBook(std::string genre, std::string book, std::string nameLoner) {
    addBook(genre,book);
    loanerByBook.insert({book, nameLoner});
    booksWantToBorrow.erase(std::remove(booksWantToBorrow.begin(), booksWantToBorrow.end(), book), booksWantToBorrow.end());
}

std::string UserBookData::printBooks(std::string genre) {
    string out = userName + ":";
    for(auto it:booksByTopic[genre])
    {
        out += it + ",";
    }
    out = out.substr(0,out.size()-1);
    return out;
}

void UserBookData::setName(std::string input) {
    std::istringstream iss(input);
    std::vector<std::string> results(std::istream_iterator<std::string>{iss},
                                     std::istream_iterator<std::string>());
    this->userName=results[2];
}

void UserBookData::lendBook(std::string topic, std::string book) {
    std::cout<<"lend book methode"<<endl;
    this->deleteBook(topic, book);
}

std::string UserBookData::getTopicBySubsID(int id) {
    for(auto it: topicBySubsID)
    {
        if(it.second==id)
            return it.first;
    }
    return "";
}

const map<std::string, int> &UserBookData::getTopicBySubsId() const {
    return topicBySubsID;
}
