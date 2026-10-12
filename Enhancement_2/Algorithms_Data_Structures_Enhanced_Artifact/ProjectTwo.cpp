//====================================================================================================
//Name            : ProjectTwo.cpp (Enhanced)
//Author          : Mason Ballard
//Class           : CS 300
//Purpose         : The purpose of this program is to read a csv file with course information. The first method will parse the file, validate the prerequisites and add
//                  the courses to a binary search tree data structure. Another method allows all the courses and their titles to be printed in alphanumeric order utilizing
//                  an in order traversal of the tree. The last method allows the user to search for a specfic course and if it exists the program will return the course along
//                  with any prerequisites.
// Updates        :
// 9/27/2026      : The original BST was modified to become a self-balancing AVL Tree and the prerequisite validation now uses an unordered set for faster performance.
//                  The original functionality remains intact, but new methods were added including getHeight and getBalance along with left and right rotations. A timer was
//                  added to test the speed of the search function but this is not required.
// 
// References     : The implementation of the AVL tree was developed based on the AVL tree concepts and implementation guidance provided by GeeksforGeeks:
//                  https://www.geeksforgeeks.org/dsa/insertion-in-an-avl-tree/
//                  *adapted and modified for use in this application
//====================================================================================================

#include <iostream>
#include <string>
#include <vector>
#include <fstream>
#include <sstream>
#include <algorithm>
#include <unordered_set>
#include <chrono>
#include <limits>
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
    int height; //
    
    //default constructor
    Node() {
        left = nullptr;
        right = nullptr;
        height = 1;
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

    Node* addNode(Node* node, Course course);
    void inOrder(Node* node);
    Node* search(Node* node, string courseNumber);
    int getHeight(Node* node);
    int getBalance(Node* node);

    Node* rightRotate(Node* y);
    Node* leftRotate(Node* x);

    void destroy(Node* node);


public:
    BinarySearchTree();
    virtual ~BinarySearchTree();

    void Insert(Course course);
    void PrintInOrder();
    Course Search(string courseNumber);
    int showHeight();

};

int BinarySearchTree::showHeight() {
    return getHeight(root);
}


//Default Constructor
BinarySearchTree::BinarySearchTree() {
    root = nullptr;
}

//Default Destructor
BinarySearchTree::~BinarySearchTree() {
    //recurse from root deleting every node
    destroy(root);
}

void BinarySearchTree::destroy(Node* node) {
    if (node == nullptr) return;
    destroy(node->left);
    destroy(node->right);
    delete node;
}


//Get height of the tree
int BinarySearchTree::getHeight(Node* node) {
    if (node == nullptr) {
        return 0;
    }
    return node->height;
}


//Get balance factor of node
// Balance factor = left height - right height
// positive is left heavy, negative is right heavy
// tree must be within -1 and 1 otherwise rotation occurs
int BinarySearchTree::getBalance(Node* node) {
    if (node == nullptr) {
        return 0;
    }
    return getHeight(node->left) - getHeight(node->right);
}


Node* BinarySearchTree::rightRotate(Node* y) {
    Node* x = y->left; //x will become new top
    Node* T2 = x->right; //save x's right side before losing it

    x->right = y; //y drops down to become x's right child
    y->left = T2; //T2 is repositioned as y's left child

    //y is now the lower node so its height is processed first
    y->height = 1 + max(getHeight(y->left), getHeight(y->right));
    x->height = 1 + max(getHeight(x->left), getHeight(x->right));

    return x; //new top
}


Node* BinarySearchTree::leftRotate(Node* x) {
    Node* y = x->right; //y will become new top
    Node* T2 = y->left; //save y's left side before losing it

    y->left = x; //x drops down to become y's left child
    x->right = T2; //T2 is repositioned as x's right child

    //x is now the lower node so its height is processed first
    x->height = 1 + max(getHeight(x->left), getHeight(x->right));
    y->height = 1 + max(getHeight(y->left), getHeight(y->right));

    return y; //new top
}

//insert a course
void BinarySearchTree::Insert(Course course) {
    root = addNode(root, course);
}


//Add a course to some node (recursive)
//@param node current node in tree
//@param course Course to be added
Node* BinarySearchTree::addNode(Node* node, Course course) {  
    if (node == nullptr) {
        return new Node(course);
    }

    //if new course is smaller than this node, go left
    if (course.courseNumber < node->course.courseNumber) {
        node->left = addNode(node->left, course);
        }
    else if (course.courseNumber > node->course.courseNumber) {
        node->right = addNode(node->right, course);
    }
    else {
        //dupliate course number, ignore it and skip height/balance calcs
        return node;
    }


    node->height = 1 + max(getHeight(node->left), getHeight(node->right));

    int balance = getBalance(node);

    //Check 4 cases

    //LL
    if (balance > 1 && course.courseNumber < node->left->course.courseNumber)
        return rightRotate(node);


    //RR
    if (balance < -1 && course.courseNumber > node->right->course.courseNumber)
        return leftRotate(node);

    //LR
    if (balance > 1 && course.courseNumber > node->left->course.courseNumber) {

        node->left = leftRotate(node->left);
        return rightRotate(node);
    }

    //RL
    if (balance < -1 && course.courseNumber < node->right->course.courseNumber) {

        node->right = rightRotate(node->right);
        return leftRotate(node);
    }

    return node;

    
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

    unordered_set<string> courseIDs;
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

        courseIDs.insert(tokens[0]);
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

            if (courseIDs.find(prereq) == courseIDs.end()) {
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

    cout << "Tree Height: " << bst.showHeight() << endl;

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

            auto start = chrono::steady_clock::now();
            Course c = bst.Search(courseNum);
            auto end = chrono::steady_clock::now();
            auto duration = chrono::duration_cast<chrono::nanoseconds>(end - start);

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

            cout << "Time to search: " << duration.count() << " ns" << endl;
            break;

        }

        


    }

    cout << "Exiting Program";
}


