//====================================================================================================
//Name            : ProjectTwo.cpp
//Author          : Mason Ballard
//Class           : CS 300
//Purpose         : The purpose of this program is to read a csv file with course information. The first method will parse the file, validate the prerequisites and add
//                  the courses to a binary search tree data structure. Another method allows all the courses and their titles to be printed in alphanumeric order utilizing
//                  an in order traversal of the tree. The last method allows the user to search for a specfic course and if it exists the program will return the course along
//                  with any prerequisites.
//====================================================================================================

#include <iostream>
#include <string>
#include <vector>
#include <fstream>
#include <sstream>
using namespace std;

//define a strcuture to hold course information
struct Course {
    string courseNumber;
    string courseName;
    vector <string> prereqs;

};

//internal structure for tree node
struct Node {
    Course course;
    Node* left;
    Node* right;
    
    //default constructor
    Node() {
        left = nullptr;
        right = nullptr;
    }

    //initialize with a course
    Node(Course aCourse) : Node() {
        course = aCourse;
    }
};


//define a class containing data members and methods to implement a binary search tree
class BinarySearchTree {
private:
    Node* root;

    void addNode(Node* node, Course course);
    void inOrder(Node* node);
    Node* search(Node* node, string courseNumber);

public:
    BinarySearchTree();
    virtual ~BinarySearchTree();

    void Insert(Course course);
    void PrintInOrder();
    Course Search(string courseNumber);
};


//Default Constructor
BinarySearchTree::BinarySearchTree() {
    root = nullptr;
}

//Default Destructor
BinarySearchTree::~BinarySearchTree() {
    //recurse from root deleting every node
    if (root != nullptr) {
        delete root->left;
        delete root->right;
        delete root;
    }
}

//insert a course
void BinarySearchTree::Insert(Course course) {
    //if root is empty
    if (root == nullptr) {
        root = new Node(course); //root is equal to new node course
    }
    else {
        this->addNode(root, course); //add Node root and course
    }
}


//Add a course to some node (recursive)
//@param node current node in tree
//@param course Course to be added
void BinarySearchTree::addNode(Node* node, Course course) {  
    //if node is larger then add to left
    if (course.courseNumber < node->course.courseNumber) {
        //if no left node
        if (node->left == nullptr) {
            node->left = new Node(course); //this node becomes left
        }
        else {
            addNode(node->left, course); //else recurse down the left node
        }
    }
    //else then add to right
    else {
        if (node->right == nullptr) { //if no right node
            node->right = new Node(course); //this node becomes right node
        }
        else {
            addNode(node->right, course); //else recurse down the right node
        }
    }
}

//Traverse the tree in order
void BinarySearchTree::PrintInOrder() {
    cout << "Printing courses in Alphanumeric Order:\n";
    //call inOrder function and pass root
    inOrder(root);
}

void BinarySearchTree::inOrder(Node* node) {
    //if node is not equal to null ptr
    if (node != nullptr) {
        inOrder(node->left); // go left
        //print the course information
        cout << node->course.courseNumber << ", " << node->course.courseName << endl;

        inOrder(node->right); //go right
    }
}

Course BinarySearchTree::Search(string courseNumber) {
    Node* node = search(root, courseNumber);

    if (node != nullptr) {
        return node->course;
    }
    else {
        return Course();
    }
}

Node* BinarySearchTree::search(Node* node, string courseNumber) {
    //if node is empty or node matches the search number return
    if (node == nullptr || node->course.courseNumber == courseNumber) {
        return node;
    }

    //if search number is less than current node go left
    if (courseNumber < node->course.courseNumber) {
        return search(node->left, courseNumber); //recurse down the left
    }

    //else go right
    return search(node->right, courseNumber); //recurse down the right
}



void loadCourses(const string& filename, BinarySearchTree& bst) {
    ifstream file(filename);
    //if file cannot be opened, print an error and exit function
    if (!file.is_open()) {
        cout << "Error: Unable to open file " << filename << endl;
        return;
    }

    vector<string> courseIDs;
    string line;

    //First Pass: collect all course IDs
    while (getline(file, line)) {
        stringstream ss(line);
        string token;
        vector<string> tokens;

        while (getline(ss, token, ',')) {
            tokens.push_back(token);
        }

        //valid course line must have at least a number,a name and is not empty
        if (tokens.size() < 2 || tokens[0].empty() || tokens[1].empty()) {
            cout << "Error: Invalid format at line: " << line << endl;
            continue;
        }

        courseIDs.push_back(tokens[0]);
    }

    file.close();

    //Second Pass: parse courses and validate prereqs
    file.open(filename);
    if (!file.is_open()) {
        cout << "Error opening file " << filename << endl;
        return;
    }

    while (getline(file, line)) {
        stringstream ss(line);
        string token;
        vector<string> tokens;
    
        while (getline(ss, token, ',')) {
            tokens.push_back(token);
        }

        if (tokens.size() < 2 || tokens[0].empty() || tokens[1].empty()) {
            cout << "error at line : " << line << endl;
            continue;
        }

        //create a new course object from the parsed data
        Course newCourse;
        newCourse.courseNumber = tokens[0];
        newCourse.courseName = tokens[1];


        //iterates through remaining tokens for prereqs
        for (size_t i = 2; i < tokens.size(); ++i) {
            string prereq = tokens[i];

            //ignore empty tokens
            if (prereq.empty()) {
                continue;
            }

            //validate the prereqs exist in the list gathered in the first pass
            bool valid = false;
            for (const string& id : courseIDs) {
                if (prereq == id) {
                    valid = true;
                    break; //found a match
                }
            }
            //if prereq is not found in the master list, report it and skip adding it
            if (!valid) {
                cout << "Prerequisite " << prereq << " for course " << newCourse.courseNumber << " does not exist." << endl;
                continue;
            }
            //if valid add the prereq to the course's list
            newCourse.prereqs.push_back(prereq);
        }

        //insert the course object into the tree
        bst.Insert(newCourse);
    }

    file.close();
    cout << "Courses loaded" << endl;

}

int main()
{
    BinarySearchTree bst;
    int choice = 0;
    string filename;
    string courseNum;

    while (choice != 9) {
        cout << "MENU" << endl
            << "1. Load Courses" << endl
            << "2. Display All Courses in Order" << endl
            << "3. Print Specific Course Title and its Prerequisites" << endl
            << "9. Exit the Program" << endl
            << "Enter Choice: ";
        cin >> choice;

        //error handling and clearing buffer
        if (cin.fail()) {
            cout << "Invalid input. Enter a number." << endl;
            cin.clear();
            cin.ignore(numeric_limits<streamsize>::max(), '\n');
            choice = 0;
            continue;
        }

        switch (choice) {

        //gets filename from user and calls loadCourses function passing the filename and binary search tree
        case 1:
            cout << "Enter filename: ";
            cin.ignore(numeric_limits<streamsize>::max(), '\n');
            getline(cin, filename);
            loadCourses(filename, bst);
            break;

        //calls the printInOrder function
        case 2:
            bst.PrintInOrder();
            break;
        

        //prompts for a course number to search (e.g. CSCI100) and calls the Search function passing the course number
        case 3:
            cout << "Enter course number: ";
            cin >> courseNum;

            Course c = bst.Search(courseNum);

            //if the course is not found print message for user
            if (c.courseNumber.empty()) {
                cout << "Course not found.\n";
            }

            //else print the course information
            else {
                cout << "\nCourse: " << c.courseNumber << ", " << c.courseName << endl;

                //if there are prerequisites for a course print them out
                if (!c.prereqs.empty()) {
                    cout << "Prerequisites: ";
                    for (unsigned int i = 0; i < c.prereqs.size(); ++i) { //iteration for multiple prereqs
                        cout << c.prereqs[i];
                        if (i < c.prereqs.size() - 1) {
                            cout << ", ";
                        }
                    }
                    cout << endl;
                }
                //else display to user no prereqs
                else {
                    cout << "No prerequisites" << endl;
                }

            }

            break;

        }

        


    }

    cout << "Exiting Program";
}


