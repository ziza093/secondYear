#include <iostream>
#include <queue>
#include <vector>

struct Node {
    int value;
    Node *left, *right;
    
    Node(int v) : value(v), left(nullptr), right(nullptr) {}
};

// comparator pentru min-heap
struct Compare {
    bool operator()(Node* a, Node* b) {
        return a->value > b->value;
    }
};

Node* lep(std::vector<int>& values) {
    std::priority_queue<Node*, std::vector<Node*>, Compare> pq;
    
    //initializez coada de prioritati cu nodurile initiale
    for (int v : values) {
        pq.push(new Node(v));
    }
    

    while (pq.size() > 1) {
        Node* t1 = pq.top(); pq.pop();
        Node* t2 = pq.top(); pq.pop();
        
        Node* t = new Node(t1->value + t2->value);
        t->left = t1;
        t->right = t2;
        
        pq.push(t);
    }
    
    return pq.top();
}

//fct pentru calculul LEP
int calculateLEP(Node* root, int depth = 0) {
    if (!root) return 0;
    if (!root->left && !root->right)
        return depth * root->value;
    
    return calculateLEP(root->left, depth + 1) + calculateLEP(root->right, depth + 1);
}

//afisare arbore printr-o parcurgere inordine
//afisare pe orizontala
void printTree(Node* root, std::string indent = "") {
    if (!root) return;
    printTree(root->right, indent + "   ");
    std::cout << indent << root->value << "\n";
    printTree(root->left, indent + "   ");
}

int main() {
    std::vector<int> values = {10, 20, 30, 50, 70};
    Node* root = lep(values);
    
    std::cout << "Arborele binar rezultat:\n";
    printTree(root);
    
    std::cout << "\nLEP: " << calculateLEP(root) << "\n";
    
    return 0;
}
